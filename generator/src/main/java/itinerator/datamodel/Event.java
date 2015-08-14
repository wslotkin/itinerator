package itinerator.datamodel;

import org.joda.time.Interval;

import java.util.Objects;

public class Event {

    private final Activity activity;
    private final Interval eventTime;
    private final double travelTime;

    public Event(Activity activity, Interval eventTime, double travelTime) {
        this.activity = activity;
        this.eventTime = eventTime;
        this.travelTime = travelTime;
    }

    public Activity getActivity() {
        return activity;
    }

    public Interval getEventTime() {
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
