package com.github.wslotkin.itinerator.analysis.custom;

import com.github.wslotkin.itinerator.generator.config.EvaluationConfig;
import com.github.wslotkin.itinerator.generator.config.ImmutableEvaluationConfig;
import com.github.wslotkin.itinerator.generator.config.ImmutableItineratorConfig;
import com.github.wslotkin.itinerator.generator.config.ItineratorConfig;
import com.github.wslotkin.itinerator.generator.data.FileType;
import com.github.wslotkin.itinerator.generator.main.FileBasedItineraryGeneratorRunner;
import com.github.wslotkin.itinerator.generator.datamodel.SolverResult;

import java.io.IOException;
import java.util.List;

import static com.github.wslotkin.itinerator.generator.data.FileType.CSV;
import static com.github.wslotkin.itinerator.generator.main.FileBasedItineraryGeneratorRunner.BEIJING_DATA;

public class CustomItineraryMain {
    private static final String OUTPUT_FILE_SUFFIX = "-Output.txt";

    public static void main(String[] args) throws IOException {
        runCustomItinerary(filePath("customItineraries") + "/Sheet 1-Table 1-1",
                CSV,
                ImmutableItineratorConfig.builder().build(),
                ImmutableEvaluationConfig.builder().build(),
                BEIJING_DATA);
    }

    public static SolverResult runCustomItinerary(String fileBase,
                                                  FileType inputFileType,
                                                  ItineratorConfig itineratorConfig,
                                                  EvaluationConfig evaluationConfig,
                                                  List<String> inputDataFiles) throws IOException {
        return new FileBasedItineraryGeneratorRunner(itineratorConfig,
                new CustomItineraryGenerator(fileBase, inputFileType, evaluationConfig),
                inputDataFiles,
                fileBase + OUTPUT_FILE_SUFFIX).run();
    }

    public static String filePath(String filename) {
        //noinspection ConstantConditions
        return CustomItineraryMain.class.getClassLoader().getResource(filename).getPath();
    }
}
