package itinerator.datamodel;

public class ActivityBuilder {

    private String id = "";
    private long duration = 60L;
    private Location location = new Location(0.0, 0.0);
    private double cost = 0.0;
    private double score = 0.0;
    private ActivityType type = ActivityType.ACTIVITY;

    public ActivityBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public ActivityBuilder setDuration(long duration) {
        this.duration = duration;
        return this;
    }

    public ActivityBuilder setLocation(Location location) {
        this.location = location;
        return this;
    }

    public ActivityBuilder setCost(double cost) {
        this.cost = cost;
        return this;
    }

    public ActivityBuilder setScore(double score) {
        this.score = score;
        return this;
    }

    public ActivityBuilder setType(ActivityType type) {
        this.type = type;
        return this;
    }

    public Activity build() {
        return new Activity(id, duration, location, cost, score, type);
    }
}
