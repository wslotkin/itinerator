package com.github.wslotkin.itinerator.generator.itinerary;

import com.github.wslotkin.itinerator.generator.datamodel.Range;
import com.google.common.annotations.VisibleForTesting;

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

    public static boolean isInMealWindow(LocalDateTime eventDateTime) {
        LocalTime eventTime = eventDateTime.toLocalTime();
        return isInWindow(eventTime, BREAKFAST_WINDOW)
                || isInWindow(eventTime, LUNCH_WINDOW)
                || isInWindow(eventTime, DINNER_WINDOW);
    }

    public static boolean isInSleepWindow(LocalDateTime eventTime) {
        return isInWindow(eventTime.toLocalTime(), SLEEP_WINDOW);
    }

    public static int numberOfMealsInTimeRange(Range<LocalDateTime> timeRange) {
        return numberOfMeals(timeRange, BREAKFAST_WINDOW, LUNCH_WINDOW, DINNER_WINDOW);
    }

    public static LocalDateTime dateWithTime(LocalDateTime date, LocalTime time) {
        return LocalDateTime.of(date.toLocalDate(), time);
    }

    private static boolean isInWindow(LocalTime time, Range<LocalTime> window) {
        return window.contains(time);
    }

    @SafeVarargs
    private static int numberOfMeals(Range<LocalDateTime> timeRange, Range<LocalTime>... windowTimes) {
        int numberOfFullDays = (int) between(timeRange.getStart(), timeRange.getEnd()).toDays();
        Range<LocalTime> remainingRange = Range.of(timeRange.getStart().toLocalTime(), timeRange.getEnd().toLocalTime());

        int additionalMeals = (int) Stream.of(windowTimes).filter(remainingRange::contains).count();

        return numberOfFullDays * windowTimes.length + additionalMeals;
    }
}
