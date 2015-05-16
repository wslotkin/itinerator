package custom;

import com.google.common.collect.Iterables;
import custom.data.CustomItineraryLoader;
import custom.data.FileType;
import itinerator.datamodel.Activity;
import itinerator.datamodel.Event;
import itinerator.main.BaseMain;
import itinerator.solver.ItinerarySolver.SolverResult;
import org.joda.time.DateTime;

import java.io.IOException;
import java.util.List;

import static custom.data.FileType.CSV;
import static itinerator.solver.ItinerarySolver.generateResult;

public class CustomItineraryMain extends BaseMain {
    private static final String OUTPUT_FILE_SUFFIX = "-Output.txt";
    private static final int BUFFER_PERIOD_MINUTES = 25;

    private final String inputFilename;
    private final FileType inputFileType;

    public static void main(String[] args) throws IOException {
        runCustomItinerary(filePath("customItineraries") + "/Sheet 1-Table 1-1", CSV, BEIJING_DATA);
    }

    protected static SolverResult runCustomItinerary(String fileBase,
                                                     FileType inputFileType,
                                                     String[] dataFiles) throws IOException {
        return new CustomItineraryMain(fileBase, OUTPUT_FILE_SUFFIX, inputFileType, dataFiles).run();
    }

    public CustomItineraryMain(String fileBase, String outputFilename, FileType inputFileType, String[] dataFiles) {
        super(fileBase + outputFilename, dataFiles);
        this.inputFileType = inputFileType;
        inputFilename = fileBase + inputFileType.getExtenstion();
    }

    @Override
    protected SolverResult getResult(List<Activity> activities, DateTime startTime, DateTime endTime) throws IOException {
        List<Event> events = new CustomItineraryLoader(activities).getActivities(inputFilename, inputFileType);

        DateTime start = events.get(0).getEventTime().getStart();
        DateTime end = Iterables.getLast(events).getEventTime().getEnd().plusMinutes(BUFFER_PERIOD_MINUTES);

        return generateResult(activities, start, end, events);
    }

    protected static String filePath(String filename) {
        //noinspection ConstantConditions
        return CustomItineraryMain.class.getClassLoader().getResource(filename).getPath();
    }
}
