package itinerator.evaluator;

import itinerator.datamodel.Activity;
import itinerator.datamodel.Event;
import itinerator.datamodel.Itinerary;
import itinerator.datamodel.Location;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

public class ItineraryBuilder {

    private static final int START_OF_DAY = 8;
    private static final Interval BREAKFAST = new Interval(new LocalTime(START_OF_DAY, 0).toDateTimeToday().getMillis(), new LocalTime(10, 0).toDateTimeToday().getMillis());
    private static final Interval LUNCH = new Interval(new LocalTime(11, 0).toDateTimeToday().getMillis(), new LocalTime(13, 0).toDateTimeToday().getMillis());
    private static final Interval DINNER = new Interval(new LocalTime(18, 0).toDateTimeToday().getMillis(), new LocalTime(20, 0).toDateTimeToday().getMillis());
    private static final Interval SLEEP = new Interval(new LocalTime(22, 0).toDateTimeToday().getMillis(), new LocalTime(START_OF_DAY, 0).toDateTimeToday().plusDays(1).getMillis());


    private final DateTime startTime;
    private final List<Activity> activities;

    public ItineraryBuilder(DateTime startTime) {
        this.startTime = startTime;
        activities = new ArrayList<>();
    }

    public ItineraryBuilder addActivityAtPosition(Activity activity, int position) {
        checkArgument(position > 0, "Index for activity should be >0.");
        if (position >= activities.size()) {
            activities.add(activity);
        } else {
            activities.add(position, activity);
        }
        return this;
    }

    public Itinerary build() {
        DateTime currentDateTime = startTime;
        List<Event> events = new ArrayList<>();
        for (Activity activity : activities) {
            if (isInMealWindow(currentDateTime.toLocalTime())) {
                Activity mealToAdd = defaultMeal(activity.getLocation());
                DateTime endTimeOfActivity = currentDateTime.plusMinutes((int) mealToAdd.getDuration());
                events.add(new Event(mealToAdd, new Interval(currentDateTime, endTimeOfActivity)));
                currentDateTime = endTimeOfActivity;
            }

            if (isInSleepWindow(currentDateTime.toLocalTime())) {
                Activity sleepToAdd = defaultSleep(activity.getLocation());
                DateTime endTimeOfActivity = currentDateTime.plusMinutes((int) sleepToAdd.getDuration()).withTime(START_OF_DAY, 0, 0, 0);
                events.add(new Event(sleepToAdd, new Interval(currentDateTime, endTimeOfActivity)));
                currentDateTime = endTimeOfActivity;
            }

            DateTime endTimeOfActivity = currentDateTime.plusMinutes((int) activity.getDuration());
            events.add(new Event(activity, new Interval(currentDateTime, endTimeOfActivity)));
            currentDateTime = endTimeOfActivity;
        }

        return new Itinerary(events);
    }

    private static Activity defaultMeal(Location location) {
        return new Activity("default meal", 60L, location, 0.0, 0.0);
    }

    private static Activity defaultSleep(Location location) {
        return new Activity("default sleep", 480L, location, 0.0, 0.0);
    }

    private static boolean isInMealWindow(LocalTime eventTime) {
        long timeInMillis = eventTime.toDateTimeToday().getMillis();
        return BREAKFAST.contains(timeInMillis) || LUNCH.contains(timeInMillis) || DINNER.contains(timeInMillis);
    }

    private static boolean isInSleepWindow(LocalTime eventTime) {
        return SLEEP.contains(eventTime.toDateTimeToday().getMillis());
    }
}
