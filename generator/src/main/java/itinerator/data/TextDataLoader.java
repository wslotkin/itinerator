package itinerator.data;

import com.google.common.collect.Lists;
import itinerator.datamodel.*;

import java.time.LocalTime;
import java.util.List;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static java.time.DayOfWeek.of;

class TextDataLoader extends AbstractDataLoader {
    private static final int ID_COLUMN = 2;
    private static final int DURATION_COLUMN = 4;
    private static final int LONGITUDE_COLUMN = 5;
    private static final int LATITUDE_COLUMN = 6;
    private static final int SCORE_COLUMN = 6;
    private static final int AVERAGE_RATING_COLUMN = 13;
    private static final int HOURS_COLUMN = 14;
    private static final int TYPE_COLUMN = 17;
    private static final String MISSING_VALUE_INDICATOR = "None";

    public TextDataLoader() {
        super(FileType.TEXT);
    }

    @Override
    protected Activity parseRowElements(String[] rowElements) {
        WeeklySchedule weeklySchedule = parseHours(rowElements[HOURS_COLUMN]);
        double score = getScore(rowElements);
        Location location = ImmutableLocation.of(optionalDouble(rowElements[LATITUDE_COLUMN]),
                optionalDouble(rowElements[LONGITUDE_COLUMN]));

        ImmutableActivity.Builder activityBuilder = ImmutableActivity.builder()
                .id(rowElements[ID_COLUMN])
                .weeklySchedule(weeklySchedule)
                .score(score)
                .location(location)
                .type(ActivityType.valueOf(rowElements[TYPE_COLUMN].toUpperCase()));

        String durationString = rowElements[DURATION_COLUMN];
        if (!durationString.isEmpty()) {
            activityBuilder = activityBuilder.duration(parseLong(durationString));
        }

        return activityBuilder.build();
    }

    private static double optionalDouble(String stringValue) {
        try {
            return parseDouble(stringValue);
        } catch (NumberFormatException exception) {
            return 0.0;
        }
    }

    private static WeeklySchedule parseHours(String hoursString) {
        String trimmedString = hoursString.replace("[", "")
                .replace("]", "")
                .replace("'", "")
                .replace("\"", "");

        String[] shifts = !trimmedString.isEmpty() ? trimmedString.split(",") : new String[]{};

        List<WeeklyShift> weeklyShifts = Lists.newArrayList();
        for (String shift : shifts) {
            weeklyShifts.add(parseShift(shift));
        }

        return new WeeklySchedule(weeklyShifts);
    }

    private static double getScore(String[] rowElements) {
        String scoreString = rowElements[SCORE_COLUMN];

        if (!MISSING_VALUE_INDICATOR.equals(scoreString)) {
            return optionalDouble(scoreString);
        } else {
            return optionalDouble(rowElements[AVERAGE_RATING_COLUMN]);
        }
    }

    private static WeeklyShift parseShift(String shift) {
        String[] hoursRange = shift.split("-");

        return ImmutableWeeklyShift.of(parseTimePoint(hoursRange[0]), parseTimePoint(hoursRange[1]));
    }

    private static WeeklyTimePoint parseTimePoint(String inputString) {
        String time = inputString.trim();
        String[] timeElements = time.split(":");
        int hourOfWeek = parseInt(timeElements[0]);
        int minute = parseInt(timeElements[1]);

        // input data stored as hourOfWeek of week starting with Sunday 00:00
        int dayOfWeek = hourOfWeek / 24 != 0 ? hourOfWeek / 24 : 7;

        return ImmutableWeeklyTimePoint.of(of(dayOfWeek), LocalTime.of(hourOfWeek % 24, minute));
    }
}
