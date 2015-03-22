package itinerator.main;

import itinerator.data.DataLoader;
import itinerator.datamodel.Activity;
import itinerator.solver.ItinerarySolver.SolverResult;
import org.joda.time.DateTime;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static itinerator.itinerary.ItineraryFormatter.prettyPrint;
import static java.lang.String.format;

public abstract class BaseMain {
    protected static final String[] NYC_DATA = {"nycplaces.csv"};
    protected static final String[] BEIJING_DATA = {"beijingspots.csv", "beijingrestaurants.csv"};
    private static final int NUMBER_OF_DAYS = 2;
    private static final int STARTING_HOUR_OF_DAY = 19;

    private final String outputFilename;
    private final String[] dataFiles;

    public BaseMain(String outputFilename, String[] dataFiles) {
        this.outputFilename = outputFilename;
        this.dataFiles = dataFiles;
    }

    public void run() throws IOException {
        List<Activity> activities = loadActivities(dataFiles);
        DateTime startTime = new DateTime().withTime(STARTING_HOUR_OF_DAY, 0, 0, 0);
        DateTime endTime = startTime.plusDays(NUMBER_OF_DAYS);

        SolverResult bestResult = getResult(activities, startTime, endTime);

        printResult(bestResult);
        if (outputFilename != null) {
            logToOutputFile(bestResult, outputFilename);
        }
    }

    protected abstract SolverResult getResult(List<Activity> activities, DateTime startTime, DateTime endTime) throws IOException;

    private static void logToOutputFile(SolverResult solverResult, String filename) throws FileNotFoundException {
        PrintStream printStream = new PrintStream(new FileOutputStream(new File(filename)));
        outputResult(solverResult, printStream);
        printStream.flush();
        printStream.close();
    }

    private static List<Activity> loadActivities(String[] dataFiles) throws IOException {
        List<Activity> activities = new ArrayList<>();
        DataLoader dataLoader = new DataLoader();
        for (String dataFile : dataFiles) {
            URL dataFileUrl = BaseMain.class.getClassLoader().getResource(dataFile);
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
