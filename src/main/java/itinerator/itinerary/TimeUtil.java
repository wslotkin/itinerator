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
        Interval sleepWindow = new Interval(fromDateAndTime(eventTime, START_OF_SLEEP_WINDOW),
                fromDateAndTime(eventTime.plusDays(1), END_OF_SLEEP_WINDOW));

        return sleepWindow.contains(eventTime) || sleepWindow.contains(eventTime.plusDays(1));
    }

    public static int numberOfMealsInTimeRange(DateTime start, DateTime end) {
        Interval timeRange = new Interval(start, end);

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

    private static int numberOfMeals(Interval timeRange, LocalTime startOfMealWindow, LocalTime endOfMealWindow) {
        int numberOfMeals = 0;
        Interval mealWindow = new Interval(fromDateAndTime(timeRange.getStart(), startOfMealWindow),
                fromDateAndTime(timeRange.getStart(), endOfMealWindow));

        while (mealWindow.getStart().isBefore(timeRange.getEnd())) {
            numberOfMeals += getNumberOfOverlaps(timeRange, mealWindow);
            mealWindow = addDay(mealWindow);
        }

        return numberOfMeals;
    }

    private static DateTime fromDateAndTime(DateTime date, LocalTime time) {
        return date.withTime(time.getHourOfDay(),
                time.getMinuteOfHour(),
                time.getSecondOfMinute(),
                time.getMillisOfSecond());
    }

    private static Interval addDay(Interval interval) {
        return new Interval(interval.getStart().plusDays(1), interval.getEnd().plusDays(1));
    }

    private static int getNumberOfOverlaps(Interval first, Interval second) {
        return first.overlaps(second) ? 1 : 0;
    }
}
