package itinerator.data;

import itinerator.datamodel.Activity;
import itinerator.datamodel.ActivityType;
import itinerator.datamodel.Location;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Double.parseDouble;
import static java.lang.Long.parseLong;

public class DataLoader {

    private static final int ID_COLUMN = 0;
    private static final int LOCATION_COLUMN = 1;
    private static final int DURATION_COLUMN = 2;
    private static final int SCORE_COLUMN = 3;
    private static final int TYPE_COLUMN = 4;
    private static final int COST_COLUMN = 5;
    private static final int LATITUDE_INDEX = 0;
    private static final int LONGITUDE_INDEX = 1;
    private static final String COMMA = ",";
    private static final String SEMICOLON = ";";

    public List<Activity> loadData(String filename) throws IOException {
        List<Activity> activities = new ArrayList<>();

        BufferedReader reader = new BufferedReader(new FileReader(new File(filename)));

        reader.readLine();
        String line;
        while ((line = reader.readLine()) != null) {
            activities.add(parseLine(line));
        }

        return activities;
    }

    private static Activity parseLine(String line) {
        String[] elements = line.split(COMMA);

        String id = elements[ID_COLUMN];
        String[] coordinates = elements[LOCATION_COLUMN].split(SEMICOLON);
        Location location = new Location(parseDouble(coordinates[LATITUDE_INDEX]), parseDouble(coordinates[LONGITUDE_INDEX]));
        long duration = parseLong(elements[DURATION_COLUMN]);
        double score = parseDouble(elements[SCORE_COLUMN]);
        ActivityType type = ActivityType.valueOf(elements[TYPE_COLUMN].toUpperCase());
        double cost = elements.length > COST_COLUMN ? parseDouble(elements[COST_COLUMN]) : 0.0;

        return new Activity(id, duration, location, cost, score, type);
    }
}
