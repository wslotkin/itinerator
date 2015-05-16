package itinerator.evaluator;

import com.google.common.annotations.VisibleForTesting;
import itinerator.datamodel.Itinerary;

public class CostEvaluator implements Evaluator {

    @VisibleForTesting
    static final double COST_MULTIPLIER = -10.0;

    public double evaluate(Itinerary itinerary) {
        return COST_MULTIPLIER * itinerary.getEvents().stream()
                .mapToDouble(a -> a.getActivity().getCost())
                .sum();
    }
}
