package itinerator.evaluator;

import itinerator.datamodel.Event;
import itinerator.datamodel.Itinerary;

public class TravelEvaluator implements Evaluator {
    private final double travelTimePenalty;

    public TravelEvaluator(double travelTimePenalty) {
        this.travelTimePenalty = travelTimePenalty;
    }

    public double evaluate(Itinerary itinerary) {
        return travelTimePenalty * itinerary.getEvents().stream()
                .mapToDouble(Event::getTravelTime)
                .sum();
    }
}
