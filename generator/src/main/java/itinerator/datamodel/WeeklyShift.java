package itinerator.datamodel;

import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;

public class WeeklyShift implements Comparable<WeeklyShift> {
    private final WeeklyTimePoint startTime;
    private final WeeklyTimePoint endTime;

    public WeeklyShift(WeeklyTimePoint startTime, WeeklyTimePoint endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public WeeklyTimePoint getStartTime() {
        return startTime;
    }

    public WeeklyTimePoint getEndTime() {
        return endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WeeklyShift that = (WeeklyShift) o;
        return compareTo(that) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(startTime, endTime);
    }

    @Override
    public int compareTo(@SuppressWarnings("NullableProblems") WeeklyShift other) {
        return ComparisonChain.start()
                .compare(startTime, other.startTime)
                .compare(endTime, other.endTime)
                .result();
    }
}
