package itinerator.evaluator;

import itinerator.calculators.TravelTimeCalculator;
import itinerator.datamodel.*;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.List;

public class ItineraryBuilder {

    private static final int START_OF_DAY = 8;
    private static final Interval BREAKFAST = new Interval(new LocalTime(START_OF_DAY, 0).toDateTimeToday().getMillis(), new LocalTime(10, 0).toDateTimeToday().getMillis());
    private static final Interval LUNCH = new Interval(new LocalTime(11, 0).toDateTimeToday().getMillis(), new LocalTime(13, 0).toDateTimeToday().getMillis());
    private static final Interval DINNER = new Interval(new LocalTime(18, 0).toDateTimeToday().getMillis(), new LocalTime(20, 0).toDateTimeToday().getMillis());
    private static final Interval SLEEP = new Interval(new LocalTime(22, 0).toDateTimeToday().getMillis(), new LocalTime(START_OF_DAY, 0).toDateTimeToday().plusDays(1).getMillis());

    private final DateTime startTime;
    private final DateTime endTime;
    private final List<Activity> activities;
    private final TravelTimeCalculator travelTimeCalculator;

    public ItineraryBuilder(DateTime startTime, DateTime endTime, TravelTimeCalculator travelTimeCalculator) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.travelTimeCalculator = travelTimeCalculator;
        activities = new ArrayList<>();
    }

    public ItineraryBuilder addActivityAtPosition(Activity activity, int position) {
        if (position >= activities.size()) {
            activities.add(activity);
        } else {
            activities.add(position, activity);
        }
        return this;
    }

    public Itinerary build() {
        Event currentEvent = null;
        List<Event> events = new ArrayList<>();
        for (Activity activity : activities) {
            DateTime currentDateTime = currentEvent != null ? currentEvent.getEventTime().getEnd() : startTime;

            if (isInMealWindow(currentDateTime.toLocalTime())) {
                currentEvent = activityToEvent(currentEvent, defaultMeal(activity.getLocation()));
                if (wouldExceedEndTime(currentEvent)) break;
                events.add(currentEvent);
                currentDateTime = currentEvent.getEventTime().getEnd();
            }

            if (isInSleepWindow(currentDateTime.toLocalTime())) {
                Activity sleepToAdd = defaultSleep(activity.getLocation());
                DateTime endTimeOfActivity = currentDateTime.plusMinutes((int) sleepToAdd.getDuration()).withTime(START_OF_DAY, 0, 0, 0);
                currentEvent = new Event(sleepToAdd, new Interval(currentDateTime, endTimeOfActivity), 0.0);
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

    private static boolean isInMealWindow(LocalTime eventTime) {
        long timeInMillis = eventTime.toDateTimeToday().getMillis();
        return BREAKFAST.contains(timeInMillis) || LUNCH.contains(timeInMillis) || DINNER.contains(timeInMillis);
    }

    private static boolean isInSleepWindow(LocalTime eventTime) {
        return SLEEP.contains(eventTime.toDateTimeToday().getMillis());
    }
}
