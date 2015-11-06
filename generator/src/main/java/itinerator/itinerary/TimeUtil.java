package itinerator.itinerary;

import com.google.common.annotations.VisibleForTesting;
import itinerator.datamodel.Range;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.stream.Stream;

import static java.time.Duration.between;

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
        return numberOfMeals(timeRange, BREAKFAST_WINDOW, LUNCH_WINDOW, DINNER_WINDOW);
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

    @SafeVarargs
    private static int numberOfMeals(Range<LocalDateTime> timeRange, Range<LocalTime>... windowTimes) {
        int numberOfFullDays = (int) between(timeRange.getStart(), timeRange.getEnd()).toDays();
        Range<LocalTime> remainingRange = Range.of(timeRange.getStart().toLocalTime(), timeRange.getEnd().toLocalTime());

        int additionalMeals = (int) Stream.of(windowTimes).filter(remainingRange::contains).count();

        return numberOfFullDays * windowTimes.length + additionalMeals;
    }
}
