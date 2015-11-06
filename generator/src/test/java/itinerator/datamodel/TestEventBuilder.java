package itinerator.datamodel;

import java.time.LocalDateTime;

public class TestEventBuilder {

    private Activity activity = new ActivityBuilder().build();
    private Range<LocalDateTime> eventTime = Range.of(LocalDateTime.now(), LocalDateTime.now().plusSeconds(1));
    private double travelTime = 0.0;

    public TestEventBuilder setActivity(Activity activity) {
        this.activity = activity;
        return this;
    }

    public TestEventBuilder setEventTime(Range<LocalDateTime> eventTime) {
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
