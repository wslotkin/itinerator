package itinerator.itinerary;

import com.google.common.annotations.VisibleForTesting;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalTime;

public class TimeUtil {

    static final int START_OF_DAY = 8;
    @VisibleForTesting
    public static final LocalTime START_OF_BREAKFAST_WINDOW = new LocalTime(START_OF_DAY, 0);
    @VisibleForTesting
    public static final LocalTime END_OF_BREAKFAST_WINDOW = new LocalTime(10, 0);
    @VisibleForTesting
    public static final LocalTime START_OF_LUNCH_WINDOW = new LocalTime(11, 0);
    @VisibleForTesting
    public static final LocalTime END_OF_LUNCH_WINDOW = new LocalTime(13, 0);
    @VisibleForTesting
    public static final LocalTime START_OF_DINNER_WINDOW = new LocalTime(18, 0);
    @VisibleForTesting
    public static final LocalTime END_OF_DINNER_WINDOW = new LocalTime(20, 0);
    @VisibleForTesting
    public static final LocalTime START_OF_SLEEP_WINDOW = new LocalTime(22, 0);
    @VisibleForTesting
    static final LocalTime END_OF_SLEEP_WINDOW = new LocalTime(START_OF_DAY, 0);

    public static boolean isInMealWindow(DateTime eventTime) {
        return isInBreakfastWindow(eventTime) || isInLunchWindow(eventTime) || isInDinnerWindow(eventTime);
    }

    public static boolean isInSleepWindow(DateTime eventTime) {
        DateTime eventTimeNextDay = eventTime.plusDays(1);
        Interval sleepWindow = new Interval(fromDateAndTime(eventTime, START_OF_SLEEP_WINDOW),
                fromDateAndTime(eventTimeNextDay, END_OF_SLEEP_WINDOW));

        return sleepWindow.contains(eventTime) || sleepWindow.contains(eventTimeNextDay);
    }

    public static int numberOfMealsInTimeRange(long startTime, long endTime) {
        Interval timeRange = new Interval(startTime, endTime);

        int numberOfMeals = numberOfMeals(timeRange, START_OF_BREAKFAST_WINDOW, END_OF_BREAKFAST_WINDOW);
        numberOfMeals += numberOfMeals(timeRange, START_OF_LUNCH_WINDOW, END_OF_LUNCH_WINDOW);
        numberOfMeals += numberOfMeals(timeRange, START_OF_DINNER_WINDOW, END_OF_DINNER_WINDOW);

        return numberOfMeals;
    }

    private static boolean isInBreakfastWindow(DateTime eventTime) {
        return isInMealWindow(eventTime, START_OF_BREAKFAST_WINDOW, END_OF_BREAKFAST_WINDOW);
    }

    private static boolean isInLunchWindow(DateTime eventTime) {
        return isInMealWindow(eventTime, START_OF_LUNCH_WINDOW, END_OF_LUNCH_WINDOW);
    }

    private static boolean isInDinnerWindow(DateTime eventTime) {
        return isInMealWindow(eventTime, START_OF_DINNER_WINDOW, END_OF_DINNER_WINDOW);
    }

    private static boolean isInMealWindow(DateTime eventTime, LocalTime startOfMealWindow, LocalTime endOfMealWindow) {
        Interval mealWindow = new Interval(fromDateAndTime(eventTime, startOfMealWindow),
                fromDateAndTime(eventTime, endOfMealWindow));
        return mealWindow.contains(eventTime);
    }

    private static int numberOfMeals(Interval timeRange, LocalTime mealWindowStartTime, LocalTime mealWindowEndTime) {
        int numberOfMeals = 0;
        DateTime startOfMealWindow = fromDateAndTime(timeRange.getStart(), mealWindowStartTime);
        DateTime endOfMealWindow = fromDateAndTime(timeRange.getStart(), mealWindowEndTime);

        while (startOfMealWindow.isBefore(timeRange.getEndMillis())) {
            numberOfMeals += overlaps(timeRange, startOfMealWindow, endOfMealWindow) ? 1 : 0;
            startOfMealWindow = startOfMealWindow.plusDays(1);
            endOfMealWindow = endOfMealWindow.plus(1);
        }

        return numberOfMeals;
    }

    private static DateTime fromDateAndTime(DateTime date, LocalTime time) {
        return date.withTime(time.getHourOfDay(),
                time.getMinuteOfHour(),
                time.getSecondOfMinute(),
                time.getMillisOfSecond());
    }

    private static boolean overlaps(Interval first, DateTime secondStart, DateTime secondEnd) {
        return first.getStartMillis() < secondEnd.getMillis() && secondStart.getMillis() < first.getEndMillis();
    }
}
