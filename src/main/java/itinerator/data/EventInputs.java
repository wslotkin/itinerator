package itinerator.data;

class EventInputs {
    private final String activityId;
    private final String start;
    private final String end;
    private final String type;

    public EventInputs(String activityId, String start, String end, String type) {
        this.activityId = activityId;
        this.start = start;
        this.end = end;
        this.type = type;
    }

    public String getActivityId() {
        return activityId;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public String getType() {
        return type;
    }
}
