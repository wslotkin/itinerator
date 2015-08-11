package itinerator.data;

import itinerator.datamodel.Activity;
import itinerator.datamodel.ActivityBuilder;
import itinerator.datamodel.Location;

import static java.lang.Double.parseDouble;
import static java.lang.Long.parseLong;

class TextDataLoader extends AbstractDataLoader {

    private static final int ID_COLUMN = 2;
    private static final int DURATION_COLUMN = 4;
    private static final int LONGITUDE_COLUMN = 5;
    private static final int LATITUDE_COLUMN = 6;
    private static final int SCORE_COLUMN = 13;

    public TextDataLoader() {
        super(FileType.TEXT);
    }

    @Override
    protected Activity parseRowElements(String[] rowElements) {
        // TODO: implement hours open

        Location location = new Location(parseDouble(rowElements[LATITUDE_COLUMN]),
                parseDouble(rowElements[LONGITUDE_COLUMN]));

        ActivityBuilder activityBuilder = new ActivityBuilder()
                .setId(rowElements[ID_COLUMN])
                .setLocation(location)
                .setScore(parseDouble(rowElements[SCORE_COLUMN]));

        String durationString = rowElements[DURATION_COLUMN];
        if (!durationString.isEmpty()) {
            activityBuilder = activityBuilder.setDuration(parseLong(durationString));
        }

        return activityBuilder.build();
    }
}
