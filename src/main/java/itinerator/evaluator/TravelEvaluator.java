package itinerator.evaluator;

import itinerator.datamodel.Itinerary;

public class TravelEvaluator implements Evaluator {

    private static final double TRAVEL_TIME_MULTIPLIER = -20.0;

    public double evaluate(Itinerary itinerary) {
        return itinerary.getEvents().stream()
                .mapToDouble(a -> a.getTravelTime() * TRAVEL_TIME_MULTIPLIER)
                .sum();
    }
}
