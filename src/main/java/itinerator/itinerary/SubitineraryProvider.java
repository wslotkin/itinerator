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
        return !rangeStartsAfterEndOfEvent(start, event) && !rangeEndsBeforeStartOfEvent(end, event);
    }

    private static boolean rangeStartsAfterEndOfEvent(DateTime start, Event event) {
        return start.isAfter(event.getEventTime().getEnd());
    }

    private static boolean rangeEndsBeforeStartOfEvent(DateTime end, Event event) {
        return end.isBefore(event.getEventTime().getStart());
    }
}
