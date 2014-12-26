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
}
