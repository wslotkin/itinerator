package itinerator.evaluator;

import itinerator.datamodel.Itinerary;

public class MovementEvaluator implements Evaluator {
    private final double areaHoppingPenalty;
    private final double areaHoppingThreshold;

    public MovementEvaluator(double areaHoppingPenalty, double areaHoppingThreshold) {
        this.areaHoppingPenalty = areaHoppingPenalty;
        this.areaHoppingThreshold = areaHoppingThreshold;
    }

    public double evaluate(Itinerary itinerary) {
        return areaHoppingPenalty * itinerary.getEvents().stream()
                .filter(event -> event.getTravelTime() > areaHoppingThreshold)
                .count();
    }
}
