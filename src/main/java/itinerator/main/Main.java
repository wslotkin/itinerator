package itinerator.main;

import itinerator.data.DataLoader;
import itinerator.datamodel.Activity;
import itinerator.solver.ItinerarySolver;
import itinerator.solver.ItinerarySolver.SolverResult;
import org.joda.time.DateTime;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static itinerator.itinerary.ItineraryFormatter.prettyPrint;
import static itinerator.solver.ItinerarySolver.createSolver;
import static java.lang.String.format;

public class Main {
    private static final String[] NYC_DATA = {"nycplaces.csv"};
    private static final String[] BEIJING_DATA = {"beijingspots.csv", "beijingrestaurants.csv"};
    private static final String[] DATA_FILES = NYC_DATA;
    private static final double MUTATION_RATE = 0.2;
    private static final int POPULATION_SIZE = 1000;
    private static final int ITERATION_THRESHOLD = 100;
    private static final int NUMBER_OF_DAYS = 2;
    private static final int STARTING_HOUR_OF_DAY = 19;

    public static void main(String[] args) throws IOException {
        List<Activity> activities = loadActivities();
        DateTime startTime = new DateTime().withTime(STARTING_HOUR_OF_DAY, 0, 0, 0);
        DateTime endTime = startTime.plusDays(NUMBER_OF_DAYS);

        ItinerarySolver itinerarySolver = createSolver(activities, startTime, endTime, POPULATION_SIZE, MUTATION_RATE, ITERATION_THRESHOLD);
        itinerarySolver.run();

        printBestResult(itinerarySolver.getBestResult());
    }

    private static List<Activity> loadActivities() throws IOException {
        List<Activity> activities = new ArrayList<>();
        DataLoader dataLoader = new DataLoader();
        for (String dataFile : DATA_FILES) {
            URL dataFileUrl = Main.class.getClassLoader().getResource(dataFile);
            if (dataFileUrl != null) {
                activities.addAll(dataLoader.loadData(dataFileUrl.getPath()));
            }
        }
        return activities;
    }

    private static void printBestResult(SolverResult bestResult) {
        String outputString = "Optimization complete. Total time: %dms. Best itinerary with score %.2f:";
        System.out.println(format(outputString, bestResult.getDuration(), bestResult.getScore()));
        System.out.println(prettyPrint(bestResult.getItinerary()));
    }
}
