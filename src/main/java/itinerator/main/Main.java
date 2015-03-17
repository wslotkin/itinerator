package itinerator.main;

import com.google.common.collect.Iterables;
import itinerator.data.CustomItineraryLoader;
import itinerator.data.DataLoader;
import itinerator.datamodel.Activity;
import itinerator.datamodel.Event;
import itinerator.solver.ItinerarySolver;
import itinerator.solver.ItinerarySolver.SolverResult;
import org.joda.time.DateTime;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static itinerator.itinerary.ItineraryFormatter.prettyPrint;
import static itinerator.solver.ItinerarySolver.createSolver;
import static itinerator.solver.ItinerarySolver.generateResult;
import static java.lang.String.format;

public class Main {
    private static final String[] NYC_DATA = {"nycplaces.csv"};
    private static final String[] BEIJING_DATA = {"beijingspots.csv", "beijingrestaurants.csv"};
    private static final String[] DATA_FILES = BEIJING_DATA;
    private static final double MUTATION_RATE = 0.2;
    private static final int POPULATION_SIZE = 1000;
    private static final int ITERATION_THRESHOLD = 100;
    private static final int NUMBER_OF_DAYS = 2;
    private static final int STARTING_HOUR_OF_DAY = 19;

    public static void main(String[] args) throws IOException {
        List<Activity> activities = loadActivities();
        DateTime startTime = new DateTime().withTime(STARTING_HOUR_OF_DAY, 0, 0, 0);
        DateTime endTime = startTime.plusDays(NUMBER_OF_DAYS);

//        SolverResult bestResult = optimize(activities, startTime, endTime);
        SolverResult bestResult = createResultForCustomItinerary(activities, "customItineraries/Sheet 1-Table 1-1.csv", ",");

        printResult(bestResult);
    }

    private static SolverResult optimize(List<Activity> activities, DateTime startTime, DateTime endTime) {
        ItinerarySolver itinerarySolver = createSolver(activities, startTime, endTime, POPULATION_SIZE, MUTATION_RATE, ITERATION_THRESHOLD);
        itinerarySolver.run();
        return itinerarySolver.getBestResult();
    }

    private static SolverResult createResultForCustomItinerary(List<Activity> activities, String filename, String delimiter) throws IOException {
        List<Event> events = new CustomItineraryLoader(activities).getActivities(filename, delimiter);

        DateTime start = events.get(0).getEventTime().getStart();
        DateTime end = Iterables.getLast(events).getEventTime().getEnd();
        SolverResult solverResult = generateResult(activities, start, end, events);

        PrintStream printStream = new PrintStream(new FileOutputStream(new File(filename.split("\\.")[0] + "-Output.txt")));
        outputResult(solverResult, printStream);
        printStream.flush();
        printStream.close();

        return solverResult;
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

    private static void printResult(SolverResult result) {
        outputResult(result, System.out);
    }

    private static void outputResult(SolverResult result, PrintStream outputStream) {
        String outputString = "Optimization complete. Total time: %dms. Best itinerary with score %.2f:";
        outputStream.println(format(outputString, result.getDuration(), result.getScore()));
        outputStream.println(prettyPrint(result.getItinerary()));
    }
}
