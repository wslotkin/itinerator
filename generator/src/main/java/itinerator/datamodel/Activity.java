package itinerator.datamodel;

import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;

import static java.util.Collections.emptyList;

@Immutable
public interface Activity {
    @Default
    default String getId() {
        return "";
    }

    @Default
    default long getDuration() {
        return 60L;
    }

    @Default
    default Location getLocation() {
        return ImmutableLocation.of(0.0, 0.0);
    }

    @Default
    default double getCost() {
        return 0.0;
    }

    @Default
    default double getScore() {
        return 0.0;
    }

    @Default
    default ActivityType getType() {
        return ActivityType.ACTIVITY;
    }

    @Default
    default WeeklySchedule getWeeklySchedule() {
        return new WeeklySchedule(emptyList());
    }
}
