package itinerator.itinerary;

import com.google.common.collect.Iterables;
import com.google.common.collect.TreeMultimap;
import itinerator.calculators.RoundingTravelTimeCalculator;
import itinerator.datamodel.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.google.common.collect.Iterables.getLast;
import static com.google.common.collect.Lists.newLinkedList;
import static com.google.common.collect.Ordering.natural;
import static itinerator.datamodel.ActivityType.*;
import static itinerator.itinerary.TimeUtil.*;
import static java.lang.Math.max;
import static java.time.Duration.between;
import static java.util.Comparator.comparing;

class ItineraryBuilder {
    private static final Comparator<Activity> ARBITRARY_BUT_PREDICTABLE_ORDERING = comparing(Activity::getId);
    private static final long TARGET_HOURS_OF_SLEEP = TimeUnit.MINUTES.toHours(TARGET_MINUTES_OF_SLEEP);

    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final TreeMultimap<Integer, Activity> activities;
    private final TreeMultimap<Integer, Activity> foods;
    private final TreeMultimap<Integer, Activity> hotels;
    private final RoundingTravelTimeCalculator travelTimeCalculator;
    private final LinkedList<Event> fixedEvents;

    public ItineraryBuilder(LocalDateTime startTime,
                            LocalDateTime endTime,
                            RoundingTravelTimeCalculator travelTimeCalculator,
                            List<Event> fixedEvents) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.travelTimeCalculator = travelTimeCalculator;
        this.fixedEvents = newLinkedList(fixedEvents);
        activities = TreeMultimap.create(natural(), ARBITRARY_BUT_PREDICTABLE_ORDERING);
        foods = TreeMultimap.create(natural(), ARBITRARY_BUT_PREDICTABLE_ORDERING);
        hotels = TreeMultimap.create(natural(), ARBITRARY_BUT_PREDICTABLE_ORDERING);
        this.fixedEvents.sort(comparing(event -> event.getEventTime().getStart()));
    }

    public ItineraryBuilder addActivityAtPosition(Activity activity, Integer position) {
        switch (activity.getType()) {
            case ACTIVITY:
                activities.put(position, activity);
                break;
            case FOOD:
                foods.put(position, activity);
                break;
            case HOTEL:
                hotels.put(position, activity);
                break;
        }
        return this;
    }

    public Itinerary build() {
        List<Event> events = new ArrayList<>();
        Queue<Activity> mealQueue = newLinkedList(foods.values());

        Activity hotel = !hotels.isEmpty() ? Iterables.get(hotels.values(), 0) : null;

        for (Activity activity : activities.values()) {
            boolean wasAdded = addEvent(events, activity, mealQueue, fixedEvents, hotel);
            if (!wasAdded) break;
        }

        return new Itinerary(events);
    }

    private boolean addEvent(List<Event> runningEventList,
                             Activity activityToAdd,
                             Queue<Activity> mealQueue,
                             Queue<Event> fixedEventQueue,
                             Activity hotel) {
        Event lastEvent = getLast(runningEventList, null);
        LocalDateTime currentDateTime = lastEvent != null ? lastEvent.getEventTime().getEnd() : startTime;
        if (wouldExceedNextFixedEvent(activityToAdd, fixedEventQueue, lastEvent)) {
            Event fixedEvent = fixedEventQueue.poll();
            Activity placeholderActivity = createPlaceholderActivity(fixedEvent, lastEvent);
            if (placeholderActivity.getDuration() > 0) {
                tryAddActivity(runningEventList, lastEvent, placeholderActivity);
            }
            boolean wasAdded = tryAddActivity(runningEventList, getLast(runningEventList, null), fixedEvent.getActivity());
            return wasAdded && addEvent(runningEventList, activityToAdd, mealQueue, fixedEventQueue, hotel);
        } else if (isNotType(activityToAdd, FOOD) && isInMealWindow(currentDateTime) && isNotType(lastEvent, FOOD)) {
            Activity mealActivity = generateMeal(locationForGeneratedActivity(lastEvent, activityToAdd), mealQueue);
            boolean wasAdded = addEvent(runningEventList, mealActivity, mealQueue, fixedEventQueue, hotel);
            return wasAdded && addEvent(runningEventList, activityToAdd, mealQueue, fixedEventQueue, hotel);
        } else if (isNotType(activityToAdd, SLEEP) && isInSleepWindow(currentDateTime) && isNotType(lastEvent, SLEEP)) {
            Activity sleepActivity = createSleepActivity(currentDateTime, lastEvent, activityToAdd, hotel);
            boolean wasAdded = addEvent(runningEventList, sleepActivity, mealQueue, fixedEventQueue, hotel);
            return wasAdded && addEvent(runningEventList, activityToAdd, mealQueue, fixedEventQueue, hotel);
        } else {
            return tryAddActivity(runningEventList, lastEvent, activityToAdd);
        }
    }

    private boolean tryAddActivity(List<Event> runningEventList, Event lastEvent, Activity activityToAdd) {
        Event newEvent = activityToEvent(lastEvent, activityToAdd);
        if (!wouldExceedTime(newEvent, endTime)) {
            runningEventList.add(newEvent);
            return true;
        } else {
            return false;
        }
    }

    private boolean wouldExceedNextFixedEvent(Activity activityToAdd, Queue<Event> fixedEventQueue, Event lastEvent) {
        if (!fixedEventQueue.isEmpty()) {
            Event eventToAdd = activityToEvent(lastEvent, activityToAdd);
            Event nextFixedEvent = fixedEventQueue.peek();
            double travelTime = travelTime(activityToAdd, nextFixedEvent.getActivity());
            return wouldExceedTime(eventToAdd, nextFixedEvent.getEventTime().getStart().minusMinutes((int) travelTime));
        } else {
            return false;
        }
    }

    private Activity createPlaceholderActivity(Event fixedEvent, Event lastEvent) {
        final Location location;
        final long duration;
        if (lastEvent != null) {
            location = lastEvent.getActivity().getLocation();
            LocalDateTime endOfLastEvent = lastEvent.getEventTime().getEnd();
            double travelTimeFromLastEvent = travelTime(lastEvent.getActivity(), fixedEvent.getActivity());
            LocalDateTime timeToStartFixedEvent = fixedEvent.getEventTime().getStart().minusMinutes((int) travelTimeFromLastEvent);
            duration = between(endOfLastEvent, timeToStartFixedEvent).toMinutes();
        } else {
            location = fixedEvent.getActivity().getLocation();
            LocalDateTime timeToStartFixedEvent = fixedEvent.getEventTime().getStart();
            duration = between(startTime, timeToStartFixedEvent).toMinutes();
        }
        return createActivity("placeholder event", duration, location, PLACEHOLDER);
    }

    private Event activityToEvent(Event previousEvent, Activity activity) {
        Activity previousActivity = previousEvent != null ? previousEvent.getActivity() : null;
        double travelTime = travelTime(previousActivity, activity);
        LocalDateTime previousEventEnd = previousEvent != null ? previousEvent.getEventTime().getEnd() : startTime;
        LocalDateTime activityStart = previousEventEnd.plusMinutes((int) travelTime);
        LocalDateTime activityEnd = activityStart.plusMinutes((int) activity.getDuration());
        return new Event(activity, Range.of(activityStart, activityEnd), travelTime);
    }

    private double travelTime(Activity firstActivity, Activity secondActivity) {
        if (firstActivity != null && secondActivity != null) {
            return travelTimeCalculator.calculate(firstActivity, secondActivity);
        } else {
            return 0.0;
        }
    }

    private static boolean wouldExceedTime(Event event, LocalDateTime time) {
        return event.getEventTime().getEnd().isAfter(time);
    }

    private static boolean isNotType(Event event, ActivityType activityType) {
        return (event == null || isNotType(event.getActivity(), activityType));
    }

    private static boolean isNotType(Activity activity, ActivityType activityType) {
        return activity.getType() != activityType;
    }

    private static Activity generateMeal(Location location, Queue<Activity> mealQueue) {
        return !mealQueue.isEmpty()
                ? mealQueue.poll()
                : createActivity("default meal", 60L, location, FOOD);
    }

    private Activity createSleepActivity(LocalDateTime currentDateTime, Event lastEvent, Activity activityToAdd, Activity hotel) {
        LocalDateTime endTimeOfActivity = dateWithTime(currentDateTime.plusMinutes(TARGET_MINUTES_OF_SLEEP), LocalTime.of(START_OF_DAY, 0));
        long minutesUntilStartOfDay = between(currentDateTime, endTimeOfActivity).toMinutes();
        long duration = max(minutesUntilStartOfDay - (long) travelTime(lastEvent.getActivity(), hotel), TARGET_MINUTES_OF_SLEEP);
        if (hotel != null) {
            return new ActivityBuilder()
                    .setId("Sleep at " + hotel.getId())
                    .setLocation(hotel.getLocation())
                    .setDuration(duration)
                    .setCost(hotel.getCost())
                    .setScore(hotel.getScore() / TARGET_HOURS_OF_SLEEP)
                    .setType(SLEEP)
                    .build();
        } else {
            return createActivity("default sleep", duration, locationForGeneratedActivity(lastEvent, activityToAdd), SLEEP);
        }
    }

    private static Location locationForGeneratedActivity(Event lastEvent, Activity nextActivity) {
        return lastEvent != null ? lastEvent.getActivity().getLocation() : nextActivity.getLocation();
    }

    private static Activity createActivity(String activityId, long duration, Location location, ActivityType type) {
        return new ActivityBuilder()
                .setId(activityId)
                .setLocation(location)
                .setDuration(duration)
                .setType(type)
                .build();
    }
}
