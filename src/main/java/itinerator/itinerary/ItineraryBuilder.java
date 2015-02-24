package itinerator.itinerary;

import com.google.common.collect.Iterables;
import com.google.common.collect.TreeMultimap;
import itinerator.calculators.TravelTimeCalculator;
import itinerator.datamodel.*;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Queue;

import static com.google.common.collect.Lists.newLinkedList;
import static com.google.common.collect.Ordering.natural;
import static itinerator.datamodel.ActivityType.FOOD;
import static itinerator.datamodel.ActivityType.SLEEP;
import static itinerator.itinerary.TimeUtil.*;
import static org.joda.time.Minutes.minutesBetween;

class ItineraryBuilder {

    private static final ActivityIdComparator ARBITRARY_BUT_PREDICTABLE_ORDERING = new ActivityIdComparator();

    private final DateTime startTime;
    private final DateTime endTime;
    private final TreeMultimap<Integer, Activity> activities;
    private final TreeMultimap<Integer, Activity> foods;
    private final TravelTimeCalculator travelTimeCalculator;

    public ItineraryBuilder(DateTime startTime, DateTime endTime, TravelTimeCalculator travelTimeCalculator) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.travelTimeCalculator = travelTimeCalculator;
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
        for (Activity activity : activities.values()) {
            boolean wasAdded = addEvent(events, activity, mealQueue);
            if (!wasAdded) break;
        }

        return new Itinerary(events);
    }

    private boolean addEvent(List<Event> runningEventList, Activity activityToAdd, Queue<Activity> mealQueue) {
        Event lastEvent = Iterables.getLast(runningEventList, null);
        DateTime currentDateTime = lastEvent != null ? lastEvent.getEventTime().getEnd() : startTime;
        if (isNotType(activityToAdd, FOOD) && isInMealWindow(currentDateTime) && isNotType(lastEvent, FOOD)) {
            Activity mealActivity = generateMeal(activityToAdd.getLocation(), mealQueue);
            boolean wasAdded = addEvent(runningEventList, mealActivity, mealQueue);
            return wasAdded && addEvent(runningEventList, activityToAdd, mealQueue);
        } else if (isNotType(activityToAdd, SLEEP) && isInSleepWindow(currentDateTime) && isNotType(lastEvent, SLEEP)) {
            Activity sleepActivity = createSleepActivity(activityToAdd, currentDateTime);
            boolean wasAdded = addEvent(runningEventList, sleepActivity, mealQueue);
            return wasAdded && addEvent(runningEventList, activityToAdd, mealQueue);
        } else {
            Event newEvent = activityToEvent(lastEvent, activityToAdd);
            if (!wouldExceedEndTime(newEvent)) {
                runningEventList.add(newEvent);
                return true;
            } else {
                return false;
            }
        }
    }

    private boolean wouldExceedEndTime(Event currentEvent) {
        return currentEvent.getEventTime().getEnd().isAfter(endTime);
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
