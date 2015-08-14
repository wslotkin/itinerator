package itinerator.datamodel;

import java.util.List;
import java.util.Objects;

public class Itinerary {

    private final List<Event> events;

    public Itinerary(List<Event> events) {
        this.events = events;
    }

    public List<Event> getEvents() {
        return events;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Itinerary itinerary = (Itinerary) o;
        return Objects.equals(events, itinerary.events);
    }

    @Override
    public int hashCode() {
        return Objects.hash(events);
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
