package itinerator.datamodel;

public class TestActivityBuilder {

    private String id = "";
    private long duration = 0L;
    private Location location = new Location(0.0, 0.0);
    private double cost = 0.0;
    private double score = 0.0;
    private ActivityType type = ActivityType.ACTIVITY;

    public TestActivityBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public TestActivityBuilder setDuration(long duration) {
        this.duration = duration;
        return this;
    }

    public TestActivityBuilder setLocation(Location location) {
        this.location = location;
        return this;
    }

    public TestActivityBuilder setCost(double cost) {
        this.cost = cost;
        return this;
    }

    public TestActivityBuilder setScore(double score) {
        this.score = score;
        return this;
    }

    public TestActivityBuilder setType(ActivityType type) {
        this.type = type;
        return this;
    }

    public Activity build() {
        return new Activity(id, duration, location, cost, score, type);
    }
}
