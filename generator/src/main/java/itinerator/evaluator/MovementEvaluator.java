package itinerator.evaluator;

import com.google.common.annotations.VisibleForTesting;
import itinerator.datamodel.Itinerary;

public class MovementEvaluator implements Evaluator {

    @VisibleForTesting
    static final double AREA_HOPPING_PENALTY = -50.0;
    @VisibleForTesting
    static final double AREA_HOPPING_THRESHOLD = 15.0;

    public double evaluate(Itinerary itinerary) {
        return AREA_HOPPING_PENALTY * itinerary.getEvents().stream()
                .filter(a -> a.getTravelTime() > AREA_HOPPING_THRESHOLD)
                .count();
    }
}
