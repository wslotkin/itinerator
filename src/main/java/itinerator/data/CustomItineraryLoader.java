package itinerator.data;

import com.google.common.collect.ImmutableMap;
import itinerator.datamodel.Activity;
import itinerator.datamodel.ActivityType;
import itinerator.datamodel.Event;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Maps.uniqueIndex;
import static org.joda.time.Minutes.minutesBetween;

public class CustomItineraryLoader {

    private final ImmutableMap<String, Activity> idToActivityMap;

    public CustomItineraryLoader(List<Activity> allActivities) {
        idToActivityMap = uniqueIndex(allActivities, Activity::getId);
    }

    public List<Event> getActivities(String filename, String delimiter) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File(filename)));

        List<Event> events = new ArrayList<>();
        String inputLine = reader.readLine();
        Event mostRecentEvent = null;
        while ((inputLine = reader.readLine()) != null) {
            String[] elements = inputLine.split(delimiter);
            String activityId = elements[0];
            String type = elements[1];
            String start = elements[2];
            String end = elements[3];
            mostRecentEvent = createEvent(activityId, start, end, type, mostRecentEvent);
            events.add(mostRecentEvent);
        }

        return events;
    }

    private Event createEvent(String activityId, String start, String end, String type, Event previousEvent) {
        DateTime startTime = DateTime.parse(start);
        DateTime endTime = DateTime.parse(end);
        Activity activity = idToActivityMap.containsKey(activityId)
                ? idToActivityMap.get(activityId)
                : createActivity(activityId, type, minutesBetween(startTime, endTime).getMinutes(), previousEvent);
        if (activity != null) {
            return new Event(activity, new Interval(startTime, endTime), 0.0);
        } else {
            System.out.println("Failed to create activity with id: " + activityId);
        }
        return null;
    }

    private static Activity createActivity(String activityId, String type, int duration, Event previousEvent) {
        ActivityType activityType = ActivityType.valueOf(type);
        return new Activity(activityId, duration, previousEvent.getActivity().getLocation(), 0, 0, activityType);
    }
}
