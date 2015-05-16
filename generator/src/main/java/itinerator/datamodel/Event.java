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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        if (Double.compare(event.travelTime, travelTime) != 0) return false;
        if (activity != null ? !activity.equals(event.activity) : event.activity != null) return false;
        if (eventTime != null ? !eventTime.equals(event.eventTime) : event.eventTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = activity != null ? activity.hashCode() : 0;
        result = 31 * result + (eventTime != null ? eventTime.hashCode() : 0);
        temp = Double.doubleToLongBits(travelTime);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
