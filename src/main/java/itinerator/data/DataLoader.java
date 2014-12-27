package itinerator.data;

import itinerator.datamodel.Activity;
import itinerator.datamodel.Location;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.lang.Double.parseDouble;
import static java.lang.Long.parseLong;

public class DataLoader {

    private static final int ID_COLUMN = 0;
    private static final int LOCATION_COLUMN = 1;
    private static final int DURATION_COLUMN = 2;
    private static final int SCORE_COLUMN = 3;
    private static final int LATITUDE_INDEX = 0;
    private static final int LONGITUDE_INDEX = 1;

    public Collection<Activity> loadData(String filename) throws IOException {
        List<Activity> activities = new ArrayList<>();

        BufferedReader reader = new BufferedReader(new FileReader(new File(filename)));
        @SuppressWarnings("UnusedAssignment") // need to discard header line
                String line = reader.readLine();
        while ((line = reader.readLine()) != null) {
            activities.add(parseLine(line));
        }

        return activities;
    }

    private Activity parseLine(String line) {
        String[] elements = line.split(",");

        String id = elements[ID_COLUMN];
        String[] coordinates = elements[LOCATION_COLUMN].split(";");
        Location location = new Location(parseDouble(coordinates[LATITUDE_INDEX]), parseDouble(coordinates[LONGITUDE_INDEX]));
        long duration = parseLong(elements[DURATION_COLUMN]);
        double cost = 0.0;
        double score = parseDouble(elements[SCORE_COLUMN]);

        return new Activity(id, duration, location, cost, score);
    }
}
