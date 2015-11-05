package itinerator.itinerary;

import itinerator.datamodel.Event;
import itinerator.datamodel.Itinerary;
import itinerator.datamodel.Range;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.time.format.DateTimeFormatter.ofPattern;

public class ItineraryFormatter {
    public static final DateTimeFormatter DATE_TIME_FORMATTER = ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final int MAX_EVENT_IDENTIFIER = 35;

    public static String prettyPrint(Itinerary itinerary) {
        StringBuilder builder = new StringBuilder();
        for (Event event : itinerary.getEvents()) {
            String eventName = event.getActivity().getId() + " (" + event.getActivity().getType() + ")";
            String truncatedName = eventName.substring(0, Math.min(MAX_EVENT_IDENTIFIER, eventName.length()));
            String filler = createFillerString(truncatedName);
            builder.append(truncatedName)
                    .append(filler)
                    .append("-\t")
                    .append(intervalToString(event.getEventTime()))
                    .append("\n");
        }
        return builder.toString();
    }

    private static String createFillerString(String eventName) {
        int excessCharsToFill = MAX_EVENT_IDENTIFIER - eventName.length();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < excessCharsToFill; i++) {
            builder.append(" ");
        }
        return builder.toString();
    }

    private static String intervalToString(Range<LocalDateTime> range) {
        return "[" + DATE_TIME_FORMATTER.format(range.getStart()) + " - " + DATE_TIME_FORMATTER.format(range.getEnd()) + "]";
    }
}
