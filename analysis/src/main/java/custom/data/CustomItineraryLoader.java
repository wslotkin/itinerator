package custom.data;

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
import static itinerator.datamodel.ActivityType.SLEEP;
import static itinerator.itinerary.TimeUtil.START_OF_BREAKFAST_WINDOW;
import static java.lang.Math.abs;
import static org.joda.time.Minutes.minutesBetween;

public class CustomItineraryLoader {

    private final Map<String, Activity> idToActivityMap;

    public CustomItineraryLoader(List<Activity> allActivities) {
        idToActivityMap = uniqueIndex(allActivities, Activity::getId);
    }

    public List<Event> getActivities(String filename, FileType fileType) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File(filename)));

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
            return eventForActivity(startTime, endTime, activity);
        } else {
            System.out.println("Failed to create activity with id: " + inputs.getActivityId());
        }
        return null;
    }

    private Event eventForActivity(DateTime startTime, DateTime endTime, Activity activity) {
        if (activity.getType() == SLEEP) {
            // This is a temporary hack to account for the fact that the original itineraries were generated using the
            // convention that generated events shared a location with the NEXT event instead of the current convention
            // of sharing the location of the PREVIOUS event. This isn't necessary for any itineraries generated after 5/16/2015
            int minutesToShift = minutesBetween(START_OF_BREAKFAST_WINDOW, endTime.toLocalTime()).getMinutes();
            return new Event(activity, new Interval(startTime.minusMinutes(minutesToShift), endTime.minusMinutes(minutesToShift)), 0.0);
        } else {
            return new Event(activity, new Interval(startTime, endTime), 0.0);
        }
    }

    static Activity createActivity(String activityId, String type, int duration, Event previousEvent) {
        ActivityType activityType = ActivityType.valueOf(type);
        return new Activity(activityId, duration, previousEvent.getActivity().getLocation(), 0, 0, activityType);
    }

    public interface CustomDataLoader {
        EventInputs getNextInputs() throws IOException;
    }
}
