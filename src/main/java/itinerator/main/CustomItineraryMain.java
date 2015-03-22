package itinerator.main;

import com.google.common.collect.Iterables;
import itinerator.data.CustomItineraryLoader;
import itinerator.datamodel.Activity;
import itinerator.datamodel.Event;
import itinerator.solver.ItinerarySolver.SolverResult;
import org.joda.time.DateTime;

import java.io.IOException;
import java.util.List;

import static itinerator.solver.ItinerarySolver.generateResult;

public class CustomItineraryMain extends BaseMain {
    private static final String OUTPUT_FILE_SUFFIX = "-Output.txt";
    private static final String INPUT_FILE_EXTENSION = ".csv";
    private static final String DELIMITER = ",";

    private final String inputFilename;
    private final String delimiter;

    public static void main(String[] args) throws IOException {
        String fileBase = "customItineraries/Sheet 1-Table 1-1";

        new CustomItineraryMain(fileBase, INPUT_FILE_EXTENSION, OUTPUT_FILE_SUFFIX, DELIMITER).run();
    }

    public CustomItineraryMain(String fileBase, String inputFileExtension, String outputFilename, String delimiter) {
        super(fileBase + outputFilename, BEIJING_DATA);
        this.delimiter = delimiter;
        inputFilename = fileBase + inputFileExtension;
    }

    @Override
    protected SolverResult getResult(List<Activity> activities, DateTime startTime, DateTime endTime) throws IOException {
        List<Event> events = new CustomItineraryLoader(activities).getActivities(inputFilename, delimiter);

        DateTime start = events.get(0).getEventTime().getStart();
        DateTime end = Iterables.getLast(events).getEventTime().getEnd();

        return generateResult(activities, start, end, events);
    }
}
