package itinerator.datamodel;

import org.joda.time.Interval;

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
}
