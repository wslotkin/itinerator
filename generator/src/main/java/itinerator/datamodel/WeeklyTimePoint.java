package itinerator.datamodel;

import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;

public class WeeklyTimePoint implements Comparable<WeeklyTimePoint> {
    private final int dayOfWeek;
    private final int hourOfDay;
    private final int minuteOfHour;

    public WeeklyTimePoint(int dayOfWeek, int hourOfDay, int minuteOfHour) {
        this.dayOfWeek = dayOfWeek;
        this.hourOfDay = hourOfDay;
        this.minuteOfHour = minuteOfHour;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public int getHourOfDay() {
        return hourOfDay;
    }

    public int getMinuteOfHour() {
        return minuteOfHour;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WeeklyTimePoint that = (WeeklyTimePoint) o;
        return compareTo(that) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(dayOfWeek, hourOfDay, minuteOfHour);
    }

    @Override
    public int compareTo(@SuppressWarnings("NullableProblems") WeeklyTimePoint other) {
        return ComparisonChain.start()
                .compare(dayOfWeek, other.dayOfWeek)
                .compare(hourOfDay, other.hourOfDay)
                .compare(minuteOfHour, other.minuteOfHour)
                .result();
    }
}
