package itinerator.datamodel;

import java.util.List;

public class Itinerary {

    private final List<Event> events;

    public Itinerary(List<Event> events) {
        this.events = events;
    }

    public List<Event> getEvents() {
        return events;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Event event : events) {
            builder.append(event.getActivity().getId())
                    .append("-")
                    .append(event.getEventTime())
                    .append("\n");
        }
        return builder.toString();
    }
}
