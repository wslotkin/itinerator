package itinerator.datamodel;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalTime;

import java.util.Collection;
import java.util.Objects;
import java.util.SortedSet;

import static com.google.common.collect.Sets.newTreeSet;
import static itinerator.datamodel.Day.valueOf;

public class WeeklySchedule {
    private final SortedSet<WeeklyShift> shifts;

    public WeeklySchedule(Collection<WeeklyShift> shifts) {
        this.shifts = newTreeSet(shifts);
    }

    public boolean isValid(Interval interval) {
        // will: this covers the case of missing hours of operation
        if (shifts.isEmpty()) return true;

        DateTime start = interval.getStart();
        DateTime end = interval.getEnd();
        WeeklyTimePoint startPoint = new WeeklyTimePoint(valueOf(start.dayOfWeek().get()),
                new LocalTime(start.hourOfDay().get(), start.minuteOfHour().get()));
        WeeklyTimePoint endPoint = new WeeklyTimePoint(valueOf(end.dayOfWeek().get()),
                new LocalTime(end.hourOfDay().get(), end.minuteOfHour().get()));

        return isEventWithinAnyShift(startPoint, endPoint);
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

    private boolean isEventWithinAnyShift(WeeklyTimePoint startPoint, WeeklyTimePoint endPoint) {
        for (WeeklyShift shift : shifts) {
            if (shift.getStartTime().compareTo(startPoint) <= 0) {
                if (shift.getEndTime().compareTo(endPoint) >= 0
                        || shift.getEndTime().compareTo(shift.getStartTime()) <= 0) {
                    return true;
                } else if (shift.getEndTime().compareTo(startPoint) > 0) {
                    return false;
                }
            } else if (shift.getStartTime().compareTo(endPoint) <= 0) {
                return shift.getStartTime().equals(shift.getEndTime());
            } else if (shift.getEndTime().compareTo(shift.getStartTime()) <= 0) {
                return shift.getEndTime().compareTo(endPoint) >= 0;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        return "WeeklySchedule{" +
                "shifts=" + shifts +
                '}';
    }
}
