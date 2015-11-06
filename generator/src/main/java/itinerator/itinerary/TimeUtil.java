package itinerator.itinerary;

import com.google.common.annotations.VisibleForTesting;
import itinerator.datamodel.Range;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class TimeUtil {

    public static final int START_OF_DAY = 8;
    public static final int TARGET_MINUTES_OF_SLEEP = 480;
    @VisibleForTesting
    public static final Range<LocalTime> BREAKFAST_WINDOW = Range.of(LocalTime.of(START_OF_DAY, 0), LocalTime.of(10, 0));
    @VisibleForTesting
    public static final Range<LocalTime> LUNCH_WINDOW = Range.of(LocalTime.of(11, 0), LocalTime.of(13, 0));
    @VisibleForTesting
    public static final Range<LocalTime> DINNER_WINDOW = Range.of(LocalTime.of(18, 0), LocalTime.of(20, 0));
    @VisibleForTesting
    static final Range<LocalTime> SLEEP_WINDOW = Range.of(LocalTime.of(22, 0), LocalTime.of(START_OF_DAY, 0));

    public static boolean isInMealWindow(LocalDateTime eventTime) {
        return isInBreakfastWindow(eventTime) || isInLunchWindow(eventTime) || isInDinnerWindow(eventTime);
    }

    public static boolean isInSleepWindow(LocalDateTime eventTime) {
        return SLEEP_WINDOW.contains(eventTime.toLocalTime());
    }

    public static int numberOfMealsInTimeRange(Range<LocalDateTime> timeRange) {
        int numberOfMeals = numberOfMeals(timeRange, BREAKFAST_WINDOW.getStart(), BREAKFAST_WINDOW.getEnd());
        numberOfMeals += numberOfMeals(timeRange, LUNCH_WINDOW.getStart(), LUNCH_WINDOW.getEnd());
        numberOfMeals += numberOfMeals(timeRange, DINNER_WINDOW.getStart(), DINNER_WINDOW.getEnd());

        return numberOfMeals;
    }

    public static LocalDateTime dateWithTime(LocalDateTime date, LocalTime time) {
        return LocalDateTime.of(date.toLocalDate(), time);
    }

    private static boolean isInBreakfastWindow(LocalDateTime eventTime) {
        return BREAKFAST_WINDOW.contains(eventTime.toLocalTime());
    }

    private static boolean isInLunchWindow(LocalDateTime eventTime) {
        return LUNCH_WINDOW.contains(eventTime.toLocalTime());
    }

    private static boolean isInDinnerWindow(LocalDateTime eventTime) {
        return DINNER_WINDOW.contains(eventTime.toLocalTime());
    }

    private static int numberOfMeals(Range<LocalDateTime> timeRange, LocalTime mealWindowStartTime, LocalTime mealWindowEndTime) {
        LocalDateTime startOfMealWindow = dateWithTime(timeRange.getStart(), mealWindowStartTime);
        LocalDateTime endOfMealWindow = dateWithTime(timeRange.getStart(), mealWindowEndTime);

        int numberOfMeals = 0;
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
