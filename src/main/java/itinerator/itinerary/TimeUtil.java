package itinerator.itinerary;

import org.joda.time.Interval;
import org.joda.time.LocalTime;

public class TimeUtil {

    static final int START_OF_DAY = 8;
    private static final Interval BREAKFAST = new Interval(new LocalTime(START_OF_DAY, 0).toDateTimeToday().getMillis(), new LocalTime(10, 0).toDateTimeToday().getMillis());
    private static final Interval LUNCH = new Interval(new LocalTime(11, 0).toDateTimeToday().getMillis(), new LocalTime(13, 0).toDateTimeToday().getMillis());
    private static final Interval DINNER = new Interval(new LocalTime(18, 0).toDateTimeToday().getMillis(), new LocalTime(20, 0).toDateTimeToday().getMillis());
    private static final Interval SLEEP = new Interval(new LocalTime(22, 0).toDateTimeToday().getMillis(), new LocalTime(START_OF_DAY, 0).toDateTimeToday().plusDays(1).getMillis());

    public static boolean isInMealWindow(LocalTime eventTime) {
        long timeInMillis = eventTime.toDateTimeToday().getMillis();
        return BREAKFAST.contains(timeInMillis) || LUNCH.contains(timeInMillis) || DINNER.contains(timeInMillis);
    }

    public static boolean isInSleepWindow(LocalTime eventTime) {
        return SLEEP.contains(eventTime.toDateTimeToday().getMillis()) || SLEEP.contains(eventTime.toDateTimeToday().plusDays(1).getMillis());
    }
}
