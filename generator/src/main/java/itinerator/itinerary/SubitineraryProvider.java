package itinerator.itinerary;

import itinerator.datamodel.Event;
import itinerator.datamodel.Itinerary;
import itinerator.datamodel.Range;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SubitineraryProvider {

    public static Itinerary subitinerary(Itinerary itinerary, LocalDateTime start, LocalDateTime end) {
        Range<LocalDateTime> subitineraryRange = Range.of(start, end);
        List<Event> eventsInTimeRange = new ArrayList<>();
        for (Event event : itinerary.getEvents()) {
            if (eventFallsInTimeRange(subitineraryRange, event)) {
                eventsInTimeRange.add(event);
            } else if (rangeEndsBeforeStartOfEvent(subitineraryRange, event)) {
                break;
            }
        }

        return new Itinerary(eventsInTimeRange);
    }

    private static boolean eventFallsInTimeRange(Range<LocalDateTime> range, Event event) {
        return range.contains(event.getEventTime().getStart());
    }

    private static boolean rangeEndsBeforeStartOfEvent(Range<LocalDateTime> range, Event event) {
        return range.getEnd().isBefore(event.getEventTime().getStart());
    }
}
