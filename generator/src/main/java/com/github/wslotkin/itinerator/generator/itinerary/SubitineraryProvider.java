package com.github.wslotkin.itinerator.generator.itinerary;

import com.github.wslotkin.itinerator.generator.datamodel.Event;
import com.github.wslotkin.itinerator.generator.datamodel.ImmutableItinerary;
import com.github.wslotkin.itinerator.generator.datamodel.Itinerary;
import com.github.wslotkin.itinerator.generator.datamodel.Range;

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

        return ImmutableItinerary.of(eventsInTimeRange);
    }

    private static boolean eventFallsInTimeRange(Range<LocalDateTime> range, Event event) {
        return range.contains(event.getEventTime().getStart());
    }

    private static boolean rangeEndsBeforeStartOfEvent(Range<LocalDateTime> range, Event event) {
        return range.getEnd().isBefore(event.getEventTime().getStart());
    }
}
