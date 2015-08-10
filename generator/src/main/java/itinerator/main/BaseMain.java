package itinerator.main;

import itinerator.config.ItineratorConfig;
import itinerator.data.DataLoader;
import itinerator.datamodel.Activity;
import itinerator.solver.ItinerarySolver.SolverResult;
import org.joda.time.DateTime;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static itinerator.data.DataLoaderFactory.createForFile;
import static itinerator.itinerary.ItineraryFormatter.prettyPrint;
import static java.lang.String.format;

public abstract class BaseMain {

    private final ItineratorConfig itineratorConfig;

    public BaseMain(ItineratorConfig itineratorConfig) {
        this.itineratorConfig = itineratorConfig;
    }

    public SolverResult run() throws IOException {
        List<Activity> activities = loadActivities(itineratorConfig.getInputDataFiles());

        SolverResult bestResult = getResult(activities,
                itineratorConfig.getStartTime(),
                itineratorConfig.getEndTime());

        printResult(bestResult);
        if (itineratorConfig.getOutputFile() != null) {
            logToOutputFile(bestResult, itineratorConfig.getOutputFile());
        }

        return bestResult;
    }

    protected ItineratorConfig config() {
        return itineratorConfig;
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
        for (String dataFile : dataFiles) {
            DataLoader dataLoader = createForFile(dataFile);
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
