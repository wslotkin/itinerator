package itinerator.datamodel;

import org.joda.time.Interval;

public class TestEventBuilder {

    private Activity activity = new ActivityBuilder().build();
    private Interval eventTime = new Interval(0L, 1L);
    private double travelTime = 0.0;

    public TestEventBuilder setActivity(Activity activity) {
        this.activity = activity;
        return this;
    }

    public TestEventBuilder setEventTime(Interval eventTime) {
        this.eventTime = eventTime;
        return this;
    }

    public TestEventBuilder setTravelTime(double travelTime) {
        this.travelTime = travelTime;
        return this;
    }

    public Event build() {
        return new Event(activity, eventTime, travelTime);
    }
}
