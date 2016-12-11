package com.github.wslotkin.itinerator.generator.main;

import com.github.wslotkin.itinerator.generator.config.ItineratorConfig;
import com.github.wslotkin.itinerator.generator.data.DataLoader;
import com.github.wslotkin.itinerator.generator.datamodel.Activity;
import com.github.wslotkin.itinerator.generator.itinerary.ItineraryFormatter;
import com.github.wslotkin.itinerator.generator.solver.ItinerarySolver;

import java.io.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.github.wslotkin.itinerator.generator.data.DataLoaderFactory.createForFile;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

public abstract class BaseMain {

    private final ItineratorConfig itineratorConfig;

    public BaseMain(ItineratorConfig itineratorConfig) {
        this.itineratorConfig = itineratorConfig;
    }

    public ItinerarySolver.SolverResult run() throws IOException {
        List<Activity> activities = loadActivities(itineratorConfig.getInputDataFiles());

        List<Activity> filteredActivities = activities.stream()
                .filter(activity -> !itineratorConfig.getExcludedActivityIds().contains(activity.getId()))
                .collect(toList());

        ItinerarySolver.SolverResult bestResult = getResult(filteredActivities,
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

    protected abstract ItinerarySolver.SolverResult getResult(List<Activity> activities,
                                                              LocalDateTime startTime,
                                                              LocalDateTime endTime) throws IOException;

    private static void logToOutputFile(ItinerarySolver.SolverResult solverResult, String filename) throws FileNotFoundException {
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

    private static void printResult(ItinerarySolver.SolverResult result) {
        outputResult(result, System.out);
    }

    private static void outputResult(ItinerarySolver.SolverResult result, PrintStream outputStream) {
        String outputString = "Optimization complete. Total time: %dms. Best itinerary with score %.2f:";
        outputStream.println(format(outputString, result.getDuration(), result.getScore()));
        outputStream.println(ItineraryFormatter.prettyPrint(result.getItinerary()));
    }
}
