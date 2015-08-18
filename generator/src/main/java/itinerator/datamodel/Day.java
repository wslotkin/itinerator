package itinerator.datamodel;

import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.uniqueIndex;

public enum Day {
    // will: correct ordering required for comparisons
    MONDAY(1),
    TUESDAY(2),
    WEDNESDAY(3),
    THURSDAY(4),
    FRIDAY(5),
    SATURDAY(6),
    SUNDAY(7);

    private static final Map<Integer, Day> INDEX_TO_DAY_MAP = uniqueIndex(newArrayList(values()), day -> day.dayOfWeek);

    private final int dayOfWeek;

    Day(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public static Day valueOf(int dayOfWeek) {
        return INDEX_TO_DAY_MAP.get(dayOfWeek);
    }
}
