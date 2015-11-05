package itinerator.datamodel;

import java.time.LocalDateTime;
import java.util.Objects;

public class Event {

    private final Activity activity;
    private final Range<LocalDateTime> eventTime;
    private final double travelTime;

    public Event(Activity activity, Range<LocalDateTime> eventTime, double travelTime) {
        this.activity = activity;
        this.eventTime = eventTime;
        this.travelTime = travelTime;
    }

    public Activity getActivity() {
        return activity;
    }

    public Range<LocalDateTime> getEventTime() {
        return eventTime;
    }

    public double getTravelTime() {
        return travelTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(travelTime, event.travelTime) &&
                Objects.equals(activity, event.activity) &&
                Objects.equals(eventTime, event.eventTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(activity, eventTime, travelTime);
    }
}
