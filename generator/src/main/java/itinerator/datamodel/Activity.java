package itinerator.datamodel;

import java.util.Objects;

public class Activity {

    private final String id;
    private final long duration;
    private final Location location;
    private final double cost;
    private final double score;
    private final ActivityType type;

    Activity(String id, long duration,
             Location location,
             double cost,
             double score,
             ActivityType type) {
        this.id = id;
        this.duration = duration;
        this.location = location;
        this.cost = cost;
        this.score = score;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public long getDuration() {
        return duration;
    }

    public Location getLocation() {
        return location;
    }

    public double getCost() {
        return cost;
    }

    public double getScore() {
        return score;
    }

    public ActivityType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Activity activity = (Activity) o;
        return Objects.equals(duration, activity.duration) &&
                Objects.equals(cost, activity.cost) &&
                Objects.equals(score, activity.score) &&
                Objects.equals(id, activity.id) &&
                Objects.equals(location, activity.location) &&
                Objects.equals(type, activity.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, duration, location, cost, score, type);
    }
}
