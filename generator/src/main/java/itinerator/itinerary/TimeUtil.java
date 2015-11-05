package itinerator.itinerary;

import com.google.common.annotations.VisibleForTesting;
import itinerator.datamodel.Range;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class TimeUtil {

    public static final int START_OF_DAY = 8;
    public static final int TARGET_MINUTES_OF_SLEEP = 480;
    @VisibleForTesting
    public static final LocalTime START_OF_BREAKFAST_WINDOW = LocalTime.of(START_OF_DAY, 0);
    @VisibleForTesting
    public static final LocalTime END_OF_BREAKFAST_WINDOW = LocalTime.of(10, 0);
    @VisibleForTesting
    public static final LocalTime START_OF_LUNCH_WINDOW = LocalTime.of(11, 0);
    @VisibleForTesting
    public static final LocalTime END_OF_LUNCH_WINDOW = LocalTime.of(13, 0);
    @VisibleForTesting
    public static final LocalTime START_OF_DINNER_WINDOW = LocalTime.of(18, 0);
    @VisibleForTesting
    public static final LocalTime END_OF_DINNER_WINDOW = LocalTime.of(20, 0);
    @VisibleForTesting
    public static final LocalTime START_OF_SLEEP_WINDOW = LocalTime.of(22, 0);
    @VisibleForTesting
    static final LocalTime END_OF_SLEEP_WINDOW = LocalTime.of(START_OF_DAY, 0);

    public static boolean isInMealWindow(LocalDateTime eventTime) {
        return isInBreakfastWindow(eventTime) || isInLunchWindow(eventTime) || isInDinnerWindow(eventTime);
    }

    public static boolean isInSleepWindow(LocalDateTime eventTime) {
        return isInWindow(eventTime, START_OF_SLEEP_WINDOW, END_OF_SLEEP_WINDOW);
    }

    public static int numberOfMealsInTimeRange(Range<LocalDateTime> timeRange) {
        int numberOfMeals = numberOfMeals(timeRange, START_OF_BREAKFAST_WINDOW, END_OF_BREAKFAST_WINDOW);
        numberOfMeals += numberOfMeals(timeRange, START_OF_LUNCH_WINDOW, END_OF_LUNCH_WINDOW);
        numberOfMeals += numberOfMeals(timeRange, START_OF_DINNER_WINDOW, END_OF_DINNER_WINDOW);

        return numberOfMeals;
    }

    public static LocalDateTime dateWithTime(LocalDateTime date, LocalTime time) {
        return LocalDateTime.of(date.toLocalDate(), time);
    }

    private static boolean isInBreakfastWindow(LocalDateTime eventTime) {
        return isInWindow(eventTime, START_OF_BREAKFAST_WINDOW, END_OF_BREAKFAST_WINDOW);
    }

    private static boolean isInLunchWindow(LocalDateTime eventTime) {
        return isInWindow(eventTime, START_OF_LUNCH_WINDOW, END_OF_LUNCH_WINDOW);
    }

    private static boolean isInDinnerWindow(LocalDateTime eventTime) {
        return isInWindow(eventTime, START_OF_DINNER_WINDOW, END_OF_DINNER_WINDOW);
    }

    private static boolean isInWindow(LocalDateTime eventTime, LocalTime startOfWindow, LocalTime endOfWindow) {
        int eventSeconds = eventTime.toLocalTime().toSecondOfDay();
        int startOfWindowSeconds = startOfWindow.toSecondOfDay();
        int endOfWindowSeconds = endOfWindow.toSecondOfDay();
        if (startOfWindowSeconds < endOfWindowSeconds) {
            return eventSeconds >= startOfWindowSeconds && eventSeconds < endOfWindowSeconds;
        } else {
            return eventSeconds >= startOfWindowSeconds || eventSeconds < endOfWindowSeconds;
        }
    }

    private static int numberOfMeals(Range<LocalDateTime> timeRange, LocalTime mealWindowStartTime, LocalTime mealWindowEndTime) {
        int numberOfMeals = 0;
        LocalDateTime startOfMealWindow = dateWithTime(timeRange.getStart(), mealWindowStartTime);
        LocalDateTime endOfMealWindow = dateWithTime(timeRange.getStart(), mealWindowEndTime);

        while (startOfMealWindow.isBefore(timeRange.getEnd())) {
            numberOfMeals += overlaps(timeRange, startOfMealWindow, endOfMealWindow) ? 1 : 0;
            startOfMealWindow = startOfMealWindow.plusDays(1);
            endOfMealWindow = endOfMealWindow.plusDays(1);
        }

        return numberOfMeals;
    }

    private static boolean overlaps(Range<LocalDateTime> first, LocalDateTime secondStart, LocalDateTime secondEnd) {
        return first.getStart().isBefore(secondEnd) && secondStart.isBefore(first.getEnd());
    }
}
