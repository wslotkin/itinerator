package itinerator.evaluator;

import com.google.common.annotations.VisibleForTesting;
import itinerator.datamodel.Event;
import itinerator.datamodel.Itinerary;

public class TravelEvaluator implements Evaluator {

    @VisibleForTesting
    static final double TRAVEL_TIME_MULTIPLIER = -20.0;

    public double evaluate(Itinerary itinerary) {
        return TRAVEL_TIME_MULTIPLIER * itinerary.getEvents().stream()
                .mapToDouble(Event::getTravelTime)
                .sum();
    }
}
