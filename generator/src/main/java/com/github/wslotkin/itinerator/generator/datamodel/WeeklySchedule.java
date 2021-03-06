package com.github.wslotkin.itinerator.generator.datamodel;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.NavigableSet;
import java.util.Objects;

import static com.google.common.collect.Sets.newTreeSet;

public class WeeklySchedule {
    private final NavigableSet<WeeklyShift> shifts;

    public WeeklySchedule(Collection<? extends WeeklyShift> shifts) {
        this.shifts = newTreeSet(shifts);
    }

    public boolean isValid(Range<LocalDateTime> range) {
        // will: this covers the case of missing hours of operation
        if (shifts.isEmpty()) return true;

        WeeklyShift eventShift = ImmutableWeeklyShift.of(toTimePoint(range.getStart()), toTimePoint(range.getEnd()));

        return isEventWithinAnyShift(eventShift);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WeeklySchedule that = (WeeklySchedule) o;
        return Objects.equals(shifts, that.shifts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shifts);
    }

    @Override
    public String toString() {
        return "WeeklySchedule{" +
                "shifts=" + shifts +
                '}';
    }

    private boolean isEventWithinAnyShift(WeeklyShift eventShift) {
        WeeklyShift floor = shifts.floor(eventShift);
        WeeklyTimePoint eventEnd = eventShift.getEndTime();

        if (floor != null) {
            // nearest shift is at or before event
            return floor.getEndTime().compareTo(eventEnd) >= 0
                    || (floor.wrapsOverWeekEnd() && !eventShift.wrapsOverWeekEnd());
        } else {
            WeeklyShift lastShift = shifts.last();
            if (lastShift.getStartTime().compareTo(eventEnd) > 0) {
                // nearest shift is after event end
                return lastShift.wrapsOverWeekEnd()
                        && lastShift.getEndTime().compareTo(eventEnd) >= 0
                        && !eventShift.wrapsOverWeekEnd();
            } else {
                // nearest shift is in middle of event
                return lastShift.isTwentyFourSeven();
            }
        }
    }

    private static WeeklyTimePoint toTimePoint(LocalDateTime dateTime) {
        return ImmutableWeeklyTimePoint.of(dateTime.getDayOfWeek(), dateTime.toLocalTime());
    }
}
