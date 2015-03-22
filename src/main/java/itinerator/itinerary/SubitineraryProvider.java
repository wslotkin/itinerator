package itinerator.itinerary;

import itinerator.datamodel.Event;
import itinerator.datamodel.Itinerary;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class SubitineraryProvider {

    public static Itinerary subitinerary(Itinerary itinerary, DateTime start, DateTime end) {
        List<Event> eventsInTimeRange = new ArrayList<>();
        for (Event event : itinerary.getEvents()) {
            if (eventFallsInTimeRange(start, end, event)) {
                eventsInTimeRange.add(event);
            } else if (rangeEndsBeforeStartOfEvent(end, event)) {
                break;
            }
        }

        return new Itinerary(eventsInTimeRange);
    }

    private static boolean eventFallsInTimeRange(DateTime start, DateTime end, Event event) {
        return !rangeStartsAfterStartOfEvent(start, event) && !rangeEndsBeforeStartOfEvent(end, event);
    }

    private static boolean rangeStartsAfterStartOfEvent(DateTime start, Event event) {
        return start.isAfter(event.getEventTime().getStartMillis());
    }

    private static boolean rangeEndsBeforeStartOfEvent(DateTime end, Event event) {
        return end.isBefore(event.getEventTime().getStartMillis());
    }
}
