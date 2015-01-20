package itinerator.evaluator;

import com.google.common.annotations.VisibleForTesting;
import itinerator.datamodel.Event;
import itinerator.datamodel.Itinerary;

/**
 * Created by smatt989 on 1/19/15.
 */
public class MovementEvaluator implements Evaluator {

    @VisibleForTesting
    static final double AREA_HOPPING_PENALTY = -50.0;

    public double evaluate(Itinerary itinerary) {
        return AREA_HOPPING_PENALTY * itinerary.getEvents().stream()
                .filter(a -> a.getTravelTime() > 15)
                .count();
    }
}
