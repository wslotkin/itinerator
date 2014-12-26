package itinerator.datamodel;

public class Activity {

    private final String id;
    private final long duration;
    private final Location location;
    private final double cost;
    private final double score;

    public Activity(String id, long duration, Location location, double cost, double score) {
        this.id = id;
        this.duration = duration;
        this.location = location;
        this.cost = cost;
        this.score = score;
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
}
