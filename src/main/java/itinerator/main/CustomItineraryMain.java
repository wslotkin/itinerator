package itinerator.main;

import com.google.common.collect.Iterables;
import itinerator.data.CustomItineraryLoader;
import itinerator.data.FileType;
import itinerator.datamodel.Activity;
import itinerator.datamodel.Event;
import itinerator.solver.ItinerarySolver.SolverResult;
import org.joda.time.DateTime;

import java.io.IOException;
import java.util.List;

import static itinerator.data.FileType.CSV;
import static itinerator.solver.ItinerarySolver.generateResult;

public class CustomItineraryMain extends BaseMain {
    private static final String OUTPUT_FILE_SUFFIX = "-Output.txt";

    private final String inputFilename;
    private final FileType inputFileType;

    public static void main(String[] args) throws IOException {
        runCustomItinerary("customItineraries/Sheet 1-Table 1-1", CSV);
    }

    public static SolverResult runCustomItinerary(String fileBase, FileType inputFileType) throws IOException {
        return new CustomItineraryMain(fileBase, OUTPUT_FILE_SUFFIX, inputFileType).run();
    }

    public CustomItineraryMain(String fileBase, String outputFilename, FileType inputFileType) {
        super(fileBase + outputFilename, BEIJING_DATA);
        this.inputFileType = inputFileType;
        inputFilename = fileBase + inputFileType.getExtenstion();
    }

    @Override
    protected SolverResult getResult(List<Activity> activities, DateTime startTime, DateTime endTime) throws IOException {
        List<Event> events = new CustomItineraryLoader(activities).getActivities(inputFilename, inputFileType);

        DateTime start = events.get(0).getEventTime().getStart();
        DateTime end = Iterables.getLast(events).getEventTime().getEnd();

        return generateResult(activities, start, end, events);
    }
}
