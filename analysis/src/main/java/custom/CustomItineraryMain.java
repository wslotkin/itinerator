package custom;

import com.google.common.collect.Iterables;
import custom.data.CustomItineraryLoader;
import itinerator.config.EvaluationConfig;
import itinerator.config.ImmutableEvaluationConfig;
import itinerator.config.ImmutableItineratorConfig;
import itinerator.config.ItineratorConfig;
import itinerator.data.FileType;
import itinerator.datamodel.Activity;
import itinerator.datamodel.Event;
import itinerator.main.BaseMain;
import itinerator.solver.ItinerarySolver.SolverResult;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static itinerator.data.FileType.CSV;
import static itinerator.solver.ItinerarySolver.generateResult;

public class CustomItineraryMain extends BaseMain {
    private static final String OUTPUT_FILE_SUFFIX = "-Output.txt";
    private static final int BUFFER_PERIOD_MINUTES = 25;

    private final String inputFilename;
    private final FileType inputFileType;
    private final EvaluationConfig evaluationConfig;

    public static void main(String[] args) throws IOException {
        runCustomItinerary(filePath("customItineraries") + "/Sheet 1-Table 1-1",
                CSV,
                ImmutableItineratorConfig.builder().build(),
                ImmutableEvaluationConfig.builder().build());
    }

    protected static SolverResult runCustomItinerary(String fileBase,
                                                     FileType inputFileType,
                                                     ItineratorConfig itineratorConfig,
                                                     EvaluationConfig evaluationConfig) throws IOException {
        return new CustomItineraryMain(fileBase, OUTPUT_FILE_SUFFIX, inputFileType, itineratorConfig, evaluationConfig).run();
    }

    public CustomItineraryMain(String fileBase,
                               String outputFilename,
                               FileType inputFileType,
                               ItineratorConfig itineratorConfig, EvaluationConfig evaluationConfig) {
        super(ImmutableItineratorConfig.builder().from(itineratorConfig).outputFile(fileBase + outputFilename).build());
        this.inputFileType = inputFileType;
        this.evaluationConfig = evaluationConfig;
        inputFilename = fileBase + inputFileType.getExtenstion();
    }

    @Override
    protected SolverResult getResult(List<Activity> activities, LocalDateTime startTime, LocalDateTime endTime) throws IOException {
        List<Event> events = new CustomItineraryLoader(activities).getActivities(inputFilename, inputFileType);

        LocalDateTime start = events.get(0).getEventTime().getStart();
        LocalDateTime end = Iterables.getLast(events).getEventTime().getEnd().plusMinutes(BUFFER_PERIOD_MINUTES);

        return generateResult(activities, start, end, events, evaluationConfig);
    }

    protected static String filePath(String filename) {
        //noinspection ConstantConditions
        return CustomItineraryMain.class.getClassLoader().getResource(filename).getPath();
    }
}
