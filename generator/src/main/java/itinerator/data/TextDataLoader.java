package itinerator.data;

import itinerator.datamodel.Activity;
import itinerator.datamodel.ActivityType;
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
        // TODO: incorporate optionality (use test builders?)
        // TODO: implement hours open

        String id = rowElements[ID_COLUMN];
        Location location = new Location(parseDouble(rowElements[LATITUDE_COLUMN]), parseDouble(rowElements[LONGITUDE_COLUMN]));
        String durationString = rowElements[DURATION_COLUMN];
        long duration = !durationString.isEmpty() ? parseLong(durationString) : 60L;
        double score = parseDouble(rowElements[SCORE_COLUMN]);
        ActivityType type = ActivityType.ACTIVITY;
        double cost = 0.0;

        return new Activity(id, duration, location, cost, score, type);
    }
}
