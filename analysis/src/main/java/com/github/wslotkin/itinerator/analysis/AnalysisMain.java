package com.github.wslotkin.itinerator.analysis;

import com.github.wslotkin.itinerator.analysis.custom.CustomItineraryMain;
import com.github.wslotkin.itinerator.generator.config.EvaluationConfig;
import com.github.wslotkin.itinerator.generator.config.ImmutableEvaluationConfig;
import com.github.wslotkin.itinerator.generator.config.ImmutableItineratorConfig;
import com.github.wslotkin.itinerator.generator.config.ItineratorConfig;
import com.github.wslotkin.itinerator.generator.data.FileType;
import com.github.wslotkin.itinerator.generator.solver.ItinerarySolver;
import com.google.common.base.Joiner;

import java.io.*;
import java.util.List;

import static com.github.wslotkin.itinerator.generator.data.FileType.TEXT;
import static com.google.common.collect.Lists.newArrayList;
import static java.lang.Double.parseDouble;
import static java.lang.String.format;

public class AnalysisMain extends CustomItineraryMain {
    private static final String ROOT_DIR = filePath("beijingItineraries");
    private static final String FILE_PREFIX = "itinerary";
    private static final String PATH_PREFIX = ROOT_DIR + File.separator + FILE_PREFIX;
    private static final String DATA_FILE_SUFFIX = ".csv";
    private static final String DATA_FILE_PATH = PATH_PREFIX + DATA_FILE_SUFFIX;

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File(DATA_FILE_PATH)));
        String header = reader.readLine();

        List<String> columns = toElements(header);
        int idColumnIndex = indexForColumn(columns, "id");
        int rankColumnIndex = indexForColumn(columns, "rank");

        List<AnalysisSolverResult> results = generateResults(reader, idColumnIndex, rankColumnIndex);
        double correlation = new ScoringFunctionEvaluator().correlationToReferenceValues(results);

        System.out.println("Correlation between rank and observed scores: " + correlation);
        output(results, correlation);
    }

    public AnalysisMain(String fileBase, String outputFilename, FileType inputFileType, ItineratorConfig itineratorConfig, EvaluationConfig evaluationConfig) {
        super(fileBase, outputFilename, inputFileType, itineratorConfig, evaluationConfig);
    }

    private static List<AnalysisSolverResult> generateResults(BufferedReader reader,
                                                              int idColumnIndex,
                                                              int rankColumnIndex) throws IOException {
        String nextEntry;
        List<AnalysisSolverResult> results = newArrayList();
        while ((nextEntry = reader.readLine()) != null) {
            List<String> elementsInRow = toElements(nextEntry);
            String id = elementsInRow.get(idColumnIndex);
            double referenceScore = parseDouble(elementsInRow.get(rankColumnIndex));

            String filePathBase = Joiner.on("_").join(PATH_PREFIX, id);

            ItinerarySolver.SolverResult solverResult = runCustomItinerary(filePathBase,
                    TEXT,
                    ImmutableItineratorConfig.builder().build(),
                    ImmutableEvaluationConfig.builder().build());

            results.add(new AnalysisSolverResult(id, solverResult, referenceScore));
        }
        return results;
    }

    private static int indexForColumn(List<String> columns, String columnHeader) {
        int columnIndex = columns.indexOf(columnHeader);
        if (columnIndex == -1) {
            throw new IllegalStateException(format("Couldn't find column '%s' in file '%s'.", "id", DATA_FILE_PATH));
        }
        return columnIndex;
    }

    private static List<String> toElements(String nextEntry) {
        return newArrayList(nextEntry.split(","));
    }

    private static void output(List<AnalysisSolverResult> results, double correlation) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(PATH_PREFIX + "-Output.txt")));
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
}
