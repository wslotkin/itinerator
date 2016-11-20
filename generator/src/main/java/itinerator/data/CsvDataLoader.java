package itinerator.data;

import itinerator.datamodel.*;

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
        String[] coordinates = rowElements[LOCATION_COLUMN].split(SEMICOLON);
        Location location = ImmutableLocation.of(parseDouble(coordinates[LATITUDE_INDEX]),
                parseDouble(coordinates[LONGITUDE_INDEX]));

        ImmutableActivity.Builder activityBuilder = ImmutableActivity.builder()
                .id(rowElements[ID_COLUMN])
                .location(location)
                .duration(parseLong(rowElements[DURATION_COLUMN]))
                .score(parseDouble(rowElements[SCORE_COLUMN]))
                .type(ActivityType.valueOf(rowElements[TYPE_COLUMN].toUpperCase()));

        if (rowElements.length > COST_COLUMN) {
            activityBuilder = activityBuilder.cost(parseDouble(rowElements[COST_COLUMN]));
        }

        return activityBuilder.build();
    }
}
