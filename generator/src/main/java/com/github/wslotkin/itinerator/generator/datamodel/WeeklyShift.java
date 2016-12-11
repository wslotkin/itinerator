package com.github.wslotkin.itinerator.generator.datamodel;

import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Parameter;

import java.util.Comparator;

import static java.util.Comparator.comparing;

@Immutable
public interface WeeklyShift extends Comparable<WeeklyShift> {
    Comparator<WeeklyShift> WEEKLY_SHIFT_COMPARATOR =
            comparing(WeeklyShift::getStartTime).thenComparing(WeeklyShift::getEndTime);

    @Parameter
    WeeklyTimePoint getStartTime();

    @Parameter
    WeeklyTimePoint getEndTime();

    default boolean wrapsOverWeekEnd() {
        return getStartTime().compareTo(getEndTime()) >= 0;
    }

    default boolean isTwentyFourSeven() {
        return getStartTime().compareTo(getEndTime()) == 0;
    }


    @Override
    default int compareTo(WeeklyShift other) {
        return WEEKLY_SHIFT_COMPARATOR.compare(this, other);
    }
}
