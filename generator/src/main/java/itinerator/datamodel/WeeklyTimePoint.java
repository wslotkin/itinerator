package itinerator.datamodel;

import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Parameter;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Comparator;

import static java.util.Comparator.comparing;


@Immutable
public interface WeeklyTimePoint extends Comparable<WeeklyTimePoint> {
    Comparator<WeeklyTimePoint> TIME_POINT_COMPARATOR =
            comparing(WeeklyTimePoint::getDayOfWeek).thenComparing(WeeklyTimePoint::getTimeOfDay);

    @Parameter
    DayOfWeek getDayOfWeek();

    @Parameter
    LocalTime getTimeOfDay();

    @Override
    default int compareTo(WeeklyTimePoint other) {
        return TIME_POINT_COMPARATOR.compare(this, other);
    }
}
