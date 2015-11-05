package itinerator.datamodel;

import com.google.common.collect.ComparisonChain;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static java.time.format.DateTimeFormatter.ofPattern;


public class WeeklyTimePoint implements Comparable<WeeklyTimePoint> {
    private static final DateTimeFormatter FORMATTER = ofPattern("HH:mm");

    private final DayOfWeek dayOfWeek;
    private final LocalTime timeOfDay;

    public WeeklyTimePoint(DayOfWeek dayOfWeek, LocalTime timeOfDay) {
        this.dayOfWeek = dayOfWeek;
        this.timeOfDay = timeOfDay;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public LocalTime getTimeOfDay() {
        return timeOfDay;
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
        return Objects.hash(dayOfWeek, timeOfDay);
    }

    @Override
    public int compareTo(@SuppressWarnings("NullableProblems") WeeklyTimePoint other) {
        return ComparisonChain.start()
                .compare(dayOfWeek, other.dayOfWeek)
                .compare(timeOfDay, other.timeOfDay)
                .result();
    }

    @Override
    public String toString() {
        return "(" + dayOfWeek + " " + FORMATTER.format(timeOfDay) + ')';
    }
}
