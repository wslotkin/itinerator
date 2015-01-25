package itinerator.itinerary;

import itinerator.datamodel.Event;
import itinerator.datamodel.Itinerary;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class ItineraryFormatter {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
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

    private static String intervalToString(Interval interval) {
        return "[" + DATE_TIME_FORMATTER.print(interval.getStart()) + " - " + DATE_TIME_FORMATTER.print(interval.getEnd()) + "]";
    }
}
