package itinerator.itinerary;

import com.google.common.collect.TreeMultimap;
import itinerator.calculators.TravelTimeCalculator;
import itinerator.datamodel.*;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.google.common.collect.Ordering.natural;
import static itinerator.itinerary.TimeUtil.*;

class ItineraryBuilder {

    private static final ActivityIdComparator ARBITRARY_BUT_PREDICTABLE_ORDERING = new ActivityIdComparator();

    private final DateTime startTime;
    private final DateTime endTime;
    private final TreeMultimap<Integer, Activity> activities;
    private final TravelTimeCalculator travelTimeCalculator;

    public ItineraryBuilder(DateTime startTime, DateTime endTime, TravelTimeCalculator travelTimeCalculator) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.travelTimeCalculator = travelTimeCalculator;
        activities = TreeMultimap.<Integer, Activity>create(natural(), ARBITRARY_BUT_PREDICTABLE_ORDERING);
    }

    public ItineraryBuilder addActivityAtPosition(Activity activity, Integer position) {
        activities.put(position, activity);
        return this;
    }

    public Itinerary build() {
        Event currentEvent = null;
        List<Event> events = new ArrayList<>();
        for (Activity activity : activities.values()) {
            DateTime currentDateTime = currentEvent != null ? currentEvent.getEventTime().getEnd() : startTime;

            if (isInMealWindow(currentDateTime.toLocalTime())) {
                currentEvent = activityToEvent(currentEvent, defaultMeal(activity.getLocation()));
                if (wouldExceedEndTime(currentEvent)) break;
                events.add(currentEvent);
                currentDateTime = currentEvent.getEventTime().getEnd();
            }

            if (isInSleepWindow(currentDateTime.toLocalTime())) {
                currentEvent = createSleepEvent(activity, currentDateTime);
                if (wouldExceedEndTime(currentEvent)) break;
                events.add(currentEvent);

                currentEvent = activityToEvent(currentEvent, defaultMeal(activity.getLocation()));
                if (wouldExceedEndTime(currentEvent)) break;
                events.add(currentEvent);
            }

            currentEvent = activityToEvent(currentEvent, activity);
            if (wouldExceedEndTime(currentEvent)) break;
            events.add(currentEvent);
        }

        return new Itinerary(events);
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

    private static Activity defaultMeal(Location location) {
        return new Activity("default meal", 60L, location, 0.0, 0.0, ActivityType.FOOD);
    }

    private static Activity defaultSleep(Location location) {
        return new Activity("default sleep", 480L, location, 0.0, 0.0, ActivityType.SLEEP);
    }

    private static Event createSleepEvent(Activity activity, DateTime currentDateTime) {
        Activity sleepToAdd = defaultSleep(activity.getLocation());
        DateTime endTimeOfActivity = currentDateTime.plusMinutes((int) sleepToAdd.getDuration()).withTime(START_OF_DAY, 0, 0, 0);
        return new Event(sleepToAdd, new Interval(currentDateTime, endTimeOfActivity), 0.0);
    }

    private static class ActivityIdComparator implements Comparator<Activity> {
        @Override
        public int compare(Activity first, Activity second) {
            return first.getId().compareTo(second.getId());
        }
    }
}
