package itinerator.data;

import itinerator.datamodel.Activity;
import itinerator.datamodel.ActivityType;
import itinerator.datamodel.Location;

import static java.lang.Double.parseDouble;
import static java.lang.Long.parseLong;

class CsvDataLoader extends AbstractDataLoader {

    private static final int ID_COLUMN = 0;
    private static final int LOCATION_COLUMN = 1;
    private static final int DURATION_COLUMN = 2;
    private static final int SCORE_COLUMN = 3;
    private static final int TYPE_COLUMN = 4;
    private static final int COST_COLUMN = 5;
    private static final int LATITUDE_INDEX = 0;
    private static final int LONGITUDE_INDEX = 1;
    private static final String SEMICOLON = ";";

    public CsvDataLoader() {
        super(FileType.CSV);
    }

    @Override
    protected Activity parseRowElements(String[] rowElements) {
        String id = rowElements[ID_COLUMN];
        String[] coordinates = rowElements[LOCATION_COLUMN].split(SEMICOLON);
        Location location = new Location(parseDouble(coordinates[LATITUDE_INDEX]), parseDouble(coordinates[LONGITUDE_INDEX]));
        long duration = parseLong(rowElements[DURATION_COLUMN]);
        double score = parseDouble(rowElements[SCORE_COLUMN]);
        ActivityType type = ActivityType.valueOf(rowElements[TYPE_COLUMN].toUpperCase());
        double cost = rowElements.length > COST_COLUMN ? parseDouble(rowElements[COST_COLUMN]) : 0.0;

        return new Activity(id, duration, location, cost, score, type);
    }
}
