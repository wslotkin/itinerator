package com.github.wslotkin.itinerator.generator.main;

import com.github.wslotkin.itinerator.generator.ItineraryGenerator;
import com.github.wslotkin.itinerator.generator.ItineraryGeneratorRunner;
import com.github.wslotkin.itinerator.generator.config.ItineratorConfig;
import com.github.wslotkin.itinerator.generator.data.DataFileActivityProvider;
import com.github.wslotkin.itinerator.generator.itinerary.ItineraryFormatter;
import com.github.wslotkin.itinerator.generator.datamodel.SolverResult;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static java.lang.String.format;

public class FileBasedItineraryGeneratorRunner {
    public static final List<String> NYC_DATA = newArrayList("nycplaces.csv");
    public static final List<String> BEIJING_DATA = newArrayList("beijingspots.csv", "beijingrestaurants.csv");
    public static final List<String> SCRAPED_CHICAGO = newArrayList("scraped-data_updated_chicago_fixed.txt", "usa_chicago_restaurants_fixed.txt");
    public static final List<String> SCRAPED_BARCELONA = newArrayList("spain_barcelona_updated.txt");

    private final ItineraryGeneratorRunner itineraryGeneratorRunner;
    private final String outputFile;

    public FileBasedItineraryGeneratorRunner(ItineratorConfig itineratorConfig,
                                             ItineraryGenerator itineraryGenerator,
                                             List<String> inputDataFiles,
                                             String outputFile) {
        this.outputFile = outputFile;
        this.itineraryGeneratorRunner = new ItineraryGeneratorRunner(itineratorConfig,
                new DataFileActivityProvider(inputDataFiles),
                itineraryGenerator);
    }

    public SolverResult run() throws FileNotFoundException {
        SolverResult bestResult = itineraryGeneratorRunner.run();

        printResult(bestResult);
        if (outputFile != null) {
            logToOutputFile(bestResult, outputFile);
        }

        return bestResult;
    }

    private static void logToOutputFile(SolverResult solverResult, String filename) throws FileNotFoundException {
        PrintStream printStream = new PrintStream(new FileOutputStream(new File(filename)));
        outputResult(solverResult, printStream);
        printStream.flush();
        printStream.close();
    }

    private static void printResult(SolverResult result) {
        outputResult(result, System.out);
    }

    private static void outputResult(SolverResult result, PrintStream outputStream) {
        String outputString = "Optimization complete. Total time: %dms. Best itinerary with score %.2f:";
        outputStream.println(format(outputString, result.getDuration(), result.getScore()));
        outputStream.println(ItineraryFormatter.prettyPrint(result.getItinerary()));
    }
}
