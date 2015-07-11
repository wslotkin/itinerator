package itinerator.evaluator;

import itinerator.datamodel.Itinerary;

public class CostEvaluator implements Evaluator {
    private final double costPenalty;

    public CostEvaluator(double costPenalty) {
        this.costPenalty = costPenalty;
    }

    public double evaluate(Itinerary itinerary) {
        return costPenalty * itinerary.getEvents().stream()
                .mapToDouble(a -> a.getActivity().getCost())
                .sum();
    }
}
