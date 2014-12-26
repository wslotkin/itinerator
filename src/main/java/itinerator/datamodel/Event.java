package itinerator.datamodel;

import org.joda.time.Interval;

public class Event {

    private final Activity activity;
    private final Interval eventTime;

    public Event(Activity activity, Interval eventTime) {
        this.activity = activity;
        this.eventTime = eventTime;
    }

    public Activity getActivity() {
        return activity;
    }

    public Interval getEventTime() {
        return eventTime;
    }
}
