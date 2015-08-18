package itinerator.datamodel;

import com.google.common.collect.ComparisonChain;

import java.util.Objects;

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
        return Objects.hash(startTime, endTime);
    }

    @Override
    public int compareTo(@SuppressWarnings("NullableProblems") WeeklyShift other) {
        return ComparisonChain.start()
                .compare(startTime, other.startTime)
                .compare(endTime, other.endTime)
                .result();
    }

    @Override
    public String toString() {
        return "[" + startTime + "-" + endTime + ']';
    }
}
