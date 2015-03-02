package itinerator.itinerary;

import com.google.common.collect.Iterables;
import com.google.common.collect.TreeMultimap;
import itinerator.calculators.TravelTimeCalculator;
import itinerator.datamodel.*;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.util.*;

import static com.google.common.collect.Lists.newLinkedList;
import static com.google.common.collect.Ordering.natural;
import static itinerator.datamodel.ActivityType.*;
import static itinerator.itinerary.TimeUtil.*;
import static org.joda.time.Minutes.minutesBetween;

class ItineraryBuilder {

    private static final ActivityIdComparator ARBITRARY_BUT_PREDICTABLE_ORDERING = new ActivityIdComparator();
    private static final Comparator<Event> EVENT_COMPARATOR =
            (o1, o2) -> o2.getEventTime().getStart().compareTo(o1.getEventTime().getStart());

    private final DateTime startTime;
    private final DateTime endTime;
    private final TreeMultimap<Integer, Activity> activities;
    private final TreeMultimap<Integer, Activity> foods;
    private final TravelTimeCalculator travelTimeCalculator;
    private final List<Event> fixedEvents;

    public ItineraryBuilder(DateTime startTime,
                            DateTime endTime,
                            TravelTimeCalculator travelTimeCalculator,
                            List<Event> fixedEvents) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.travelTimeCalculator = travelTimeCalculator;
        this.fixedEvents = fixedEvents;
        activities = TreeMultimap.create(natural(), ARBITRARY_BUT_PREDICTABLE_ORDERING);
        foods = TreeMultimap.create(natural(), ARBITRARY_BUT_PREDICTABLE_ORDERING);
    }

    public ItineraryBuilder addActivityAtPosition(Activity activity, Integer position) {
        switch (activity.getType()) {
            case ACTIVITY:
                activities.put(position, activity);
                break;
            case FOOD:
                foods.put(position, activity);
                break;
        }
        return this;
    }

    public Itinerary build() {
        List<Event> events = new ArrayList<>();
        Queue<Activity> mealQueue = newLinkedList(foods.values());
        Collections.sort(fixedEvents, EVENT_COMPARATOR);
        Queue<Event> fixedEventQueue = newLinkedList(fixedEvents);
        for (Activity activity : activities.values()) {
            boolean wasAdded = addEvent(events, activity, mealQueue, fixedEventQueue);
            if (!wasAdded) break;
        }

        return new Itinerary(events);
    }

    private boolean addEvent(List<Event> runningEventList,
                             Activity activityToAdd,
                             Queue<Activity> mealQueue,
                             Queue<Event> fixedEventQueue) {
        Event lastEvent = Iterables.getLast(runningEventList, null);
        DateTime currentDateTime = lastEvent != null ? lastEvent.getEventTime().getEnd() : startTime;
        if (wouldExceedNextFixedEvent(activityToAdd, fixedEventQueue, lastEvent)) {
            Event fixedEvent = fixedEventQueue.poll();
            tryAddActivity(runningEventList, lastEvent, createPlaceholderActivity(fixedEvent, lastEvent));
            boolean wasAdded = tryAddActivity(runningEventList, lastEvent, fixedEvent.getActivity());
            return wasAdded && addEvent(runningEventList, activityToAdd, mealQueue, fixedEventQueue);
        } else if (isNotType(activityToAdd, FOOD) && isInMealWindow(currentDateTime) && isNotType(lastEvent, FOOD)) {
            Activity mealActivity = generateMeal(activityToAdd.getLocation(), mealQueue);
            boolean wasAdded = addEvent(runningEventList, mealActivity, mealQueue, fixedEventQueue);
            return wasAdded && addEvent(runningEventList, activityToAdd, mealQueue, fixedEventQueue);
        } else if (isNotType(activityToAdd, SLEEP) && isInSleepWindow(currentDateTime) && isNotType(lastEvent, SLEEP)) {
            Activity sleepActivity = createSleepActivity(activityToAdd, currentDateTime);
            boolean wasAdded = addEvent(runningEventList, sleepActivity, mealQueue, fixedEventQueue);
            return wasAdded && addEvent(runningEventList, activityToAdd, mealQueue, fixedEventQueue);
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
        final int duration;
        if (lastEvent != null) {
            location = lastEvent.getActivity().getLocation();
            DateTime endOfLastEvent = lastEvent.getEventTime().getEnd();
            double travelTimeFromLastEvent = travelTime(lastEvent.getActivity(), fixedEvent.getActivity());
            DateTime timeToStartFixedEvent = fixedEvent.getEventTime().getStart().minusMinutes((int) travelTimeFromLastEvent);
            duration = minutesBetween(endOfLastEvent, timeToStartFixedEvent).getMinutes();
        } else {
            location = fixedEvent.getActivity().getLocation();
            DateTime timeToStartFixedEvent = fixedEvent.getEventTime().getStart().minusMinutes(0);
            duration = minutesBetween(startTime, timeToStartFixedEvent).getMinutes();
        }
        return new Activity("placeholder event", duration, location, 0.0, 0.0, PLACEHOLDER);
    }

    private Event activityToEvent(Event previousEvent, Activity activity) {
        Activity previousActivity = previousEvent != null ? previousEvent.getActivity() : null;
        double travelTime = travelTime(previousActivity, activity);
        DateTime previousEventEnd = previousEvent != null ? previousEvent.getEventTime().getEnd() : startTime;
        DateTime activityStart = previousEventEnd.plusMinutes((int) travelTime);
        DateTime activityEnd = activityStart.plusMinutes((int) activity.getDuration());
        return new Event(activity, new Interval(activityStart, activityEnd), travelTime);
    }

    private double travelTime(Activity firstActivity, Activity secondActivity) {
        if (firstActivity != null && secondActivity != null) {
            return travelTimeCalculator.calculate(firstActivity, secondActivity);
        } else {
            return 0.0;
        }
    }

    private static boolean wouldExceedTime(Event event, DateTime time) {
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
                : new Activity("default meal", 60L, location, 0.0, 0.0, FOOD);
    }

    private static Activity createSleepActivity(Activity activity, DateTime currentDateTime) {
        DateTime endTimeOfActivity = currentDateTime.plusMinutes(480).withTime(START_OF_DAY, 0, 0, 0);
        long sleepDuration = minutesBetween(currentDateTime, endTimeOfActivity).getMinutes();
        return new Activity("default sleep", sleepDuration, activity.getLocation(), 0.0, 0.0, ActivityType.SLEEP);
    }

    private static class ActivityIdComparator implements Comparator<Activity> {
        @Override
        public int compare(Activity first, Activity second) {
            return first.getId().compareTo(second.getId());
        }
    }
}
