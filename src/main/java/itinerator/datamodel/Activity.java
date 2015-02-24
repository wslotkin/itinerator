package itinerator.datamodel;

public class Activity {

    private final String id;
    private final long duration;
    private final Location location;
    private final double cost;
    private final double score;
    private final ActivityType type;

    public Activity(String id, long duration, Location location, double cost, double score, ActivityType type) {
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

        if (Double.compare(activity.cost, cost) != 0) return false;
        if (duration != activity.duration) return false;
        if (Double.compare(activity.score, score) != 0) return false;
        if (id != null ? !id.equals(activity.id) : activity.id != null) return false;
        if (location != null ? !location.equals(activity.location) : activity.location != null) return false;
        if (type != activity.type) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id != null ? id.hashCode() : 0;
        result = 31 * result + (int) (duration ^ (duration >>> 32));
        result = 31 * result + (location != null ? location.hashCode() : 0);
        temp = Double.doubleToLongBits(cost);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(score);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}
