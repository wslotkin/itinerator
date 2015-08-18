package itinerator.evaluator;

import itinerator.datamodel.Itinerary;

public class HoursEvaluator implements Evaluator {
    private final double invalidHoursPenalty;

    public HoursEvaluator(double invalidHoursPenalty) {
        this.invalidHoursPenalty = invalidHoursPenalty;
    }

    @Override
    public double evaluate(Itinerary itinerary) {
        return invalidHoursPenalty * itinerary.getEvents().stream()
                .filter(event -> !event.getActivity().getWeeklySchedule().isValid(event.getEventTime()))
                .count();
    }
}
