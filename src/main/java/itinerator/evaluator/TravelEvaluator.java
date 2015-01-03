package itinerator.evaluator;

import itinerator.datamodel.Itinerary;

public class TravelEvaluator implements Evaluator {
    public double evaluate(Itinerary itinerary) {
        return itinerary.getEvents().stream()
                .mapToDouble(a -> a.getTravelTime() * -20.0)
                .sum();
    }
}
