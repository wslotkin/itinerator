package itinerator.data;

import itinerator.datamodel.Activity;
import itinerator.datamodel.ActivityType;
import itinerator.datamodel.Event;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.uniqueIndex;
import static org.joda.time.Minutes.minutesBetween;

public class CustomItineraryLoader {

    private final Map<String, Activity> idToActivityMap;

    public CustomItineraryLoader(List<Activity> allActivities) {
        idToActivityMap = uniqueIndex(allActivities, Activity::getId);
    }

    public List<Event> getActivities(String filename, FileType fileType) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File(filename)));
        reader.readLine();

        CustomDataLoader dataLoader = getDataLoader(fileType, reader);

        List<Event> events = newArrayList();
        Event previousEvent = null;
        EventInputs inputs;
        while ((inputs = dataLoader.getNextInputs()) != null) {
            Event event = createEvent(inputs, previousEvent);
            events.add(event);
            previousEvent = event;
        }
        return events;
    }

    private CustomDataLoader getDataLoader(FileType fileType, BufferedReader reader) {
        switch (fileType) {
            case CSV:
                return new CsvItineraryLoader(reader);
            case TEXT:
                return new TextItineraryLoader(reader);
        }
        throw new IllegalArgumentException("Unexpected file type: " + fileType);
    }

    private Event createEvent(EventInputs inputs, Event previousEvent) {
        DateTime startTime = DateTime.parse(inputs.getStart());
        DateTime endTime = DateTime.parse(inputs.getEnd());
        Activity activity = idToActivityMap.containsKey(inputs.getActivityId())
                ? idToActivityMap.get(inputs.getActivityId())
                : createActivity(inputs.getActivityId(), inputs.getType(), minutesBetween(startTime, endTime).getMinutes(), previousEvent);
        if (activity != null) {
            return new Event(activity, new Interval(startTime, endTime), 0.0);
        } else {
            System.out.println("Failed to create activity with id: " + inputs.getActivityId());
        }
        return null;
    }

    static Activity createActivity(String activityId, String type, int duration, Event previousEvent) {
        ActivityType activityType = ActivityType.valueOf(type);
        return new Activity(activityId, duration, previousEvent.getActivity().getLocation(), 0, 0, activityType);
    }

    public interface CustomDataLoader {
        EventInputs getNextInputs() throws IOException;
    }
}
