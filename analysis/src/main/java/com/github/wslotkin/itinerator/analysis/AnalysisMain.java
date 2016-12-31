package com.github.wslotkin.itinerator.analysis;

import com.github.wslotkin.itinerator.generator.config.ImmutableEvaluationConfig;
import com.github.wslotkin.itinerator.generator.config.ImmutableItineratorConfig;
import com.github.wslotkin.itinerator.generator.data.FileType;
import com.github.wslotkin.itinerator.generator.datamodel.SolverResult;
import com.google.common.base.Joiner;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static com.github.wslotkin.itinerator.analysis.custom.CustomItineraryMain.filePath;
import static com.github.wslotkin.itinerator.analysis.custom.CustomItineraryMain.runCustomItinerary;
import static com.github.wslotkin.itinerator.generator.data.FileType.CSV;
import static com.github.wslotkin.itinerator.generator.data.FileType.TEXT;
import static com.github.wslotkin.itinerator.generator.main.FileBasedItineraryGeneratorRunner.BEIJING_DATA;
import static com.google.common.collect.Lists.newArrayList;
import static java.lang.Double.parseDouble;
import static java.lang.String.format;

public class AnalysisMain {
    private static final String GENERATED_ROOT_DIR = filePath("beijingItineraries");
    private static final String MANUAL_ROOT_DIR = filePath("customItineraries");
    private static final String FILE_PREFIX = "itinerary";

    public static void main(String[] args) throws IOException {
        List<AnalysisSolverResult> results = new ArrayList<>();
        results.addAll(getAnalysisSolverResultsForPath(GENERATED_ROOT_DIR, TEXT));
        results.addAll(getAnalysisSolverResultsForPath(MANUAL_ROOT_DIR, CSV));
        double correlation = new ScoringFunctionEvaluator().correlationToReferenceValues(results);

        System.out.println("Correlation between rank and observed scores: " + correlation);
        output(results, correlation, filePathPrefix(GENERATED_ROOT_DIR));
    }

    private static List<AnalysisSolverResult> getAnalysisSolverResultsForPath(String rootDir,
                                                                              FileType inputFileType) throws IOException {
        String pathPrefix = filePathPrefix(rootDir);
        String dataFilePath = pathPrefix + CSV.getExtenstion();
        BufferedReader reader = new BufferedReader(new FileReader(new File(dataFilePath)));
        String header = reader.readLine();

        List<String> columns = toElements(header);
        int idColumnIndex = indexForColumn(columns, "id", dataFilePath);
        int rankColumnIndex = indexForColumn(columns, "rank", dataFilePath);

        return generateResults(reader, idColumnIndex, rankColumnIndex, inputFileType, pathPrefix);
    }

    private static List<AnalysisSolverResult> generateResults(BufferedReader reader,
                                                              int idColumnIndex,
                                                              int rankColumnIndex,
                                                              FileType inputFileType,
                                                              String pathPrefix) throws IOException {
        String nextEntry;
        List<AnalysisSolverResult> results = newArrayList();
        while ((nextEntry = reader.readLine()) != null) {
            List<String> elementsInRow = toElements(nextEntry);
            String id = elementsInRow.get(idColumnIndex);
            double referenceScore = parseDouble(elementsInRow.get(rankColumnIndex));

            String filePathBase = Joiner.on("_").join(pathPrefix, id);

            SolverResult solverResult = runCustomItinerary(filePathBase,
                    inputFileType,
                    ImmutableItineratorConfig.builder().build(),
                    ImmutableEvaluationConfig.builder().build(),
                    BEIJING_DATA);

            results.add(new AnalysisSolverResult(id, solverResult, referenceScore));
        }
        return results;
    }

    private static int indexForColumn(List<String> columns, String columnHeader, String dataFilePath) {
        int columnIndex = columns.indexOf(columnHeader);
        if (columnIndex == -1) {
            throw new IllegalStateException(format("Couldn't find column '%s' in file '%s'.", "id", dataFilePath));
        }
        return columnIndex;
    }

    private static List<String> toElements(String nextEntry) {
        return newArrayList(nextEntry.split(","));
    }

    private static void output(List<AnalysisSolverResult> results, double correlation, String pathPrefix) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(pathPrefix + "-Output.txt")));
        bufferedWriter.write("Correlation between rank and observed scores: " + correlation);
        bufferedWriter.newLine();
        bufferedWriter.write("id\t\t\tobserved score\treference rank");
        bufferedWriter.newLine();

        for (AnalysisSolverResult result : results) {
            bufferedWriter.write(String.format("%s\t\t%.2f\t\t\t%.2f",
                    result.getId(),
                    result.getResult().getScore(),
                    result.getReferenceScore()));
            bufferedWriter.newLine();
        }

        bufferedWriter.flush();
        bufferedWriter.close();
    }

    private static String filePathPrefix(String rootDir) {
        return rootDir + File.separator + FILE_PREFIX;
    }
}
