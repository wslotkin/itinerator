package itinerator.datamodel;

import com.google.common.collect.ComparisonChain;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormatter;

import java.util.Objects;

import static org.joda.time.format.DateTimeFormat.forPattern;

public class WeeklyTimePoint implements Comparable<WeeklyTimePoint> {
    private static final DateTimeFormatter FORMATTER = forPattern("HH:mm");

    private final Day dayOfWeek;
    private final LocalTime timeOfDay;

    public WeeklyTimePoint(Day dayOfWeek, LocalTime timeOfDay) {
        this.dayOfWeek = dayOfWeek;
        this.timeOfDay = timeOfDay;
    }

    public Day getDayOfWeek() {
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
        return "(" + dayOfWeek + " " + FORMATTER.print(timeOfDay) + ')';
    }
}
