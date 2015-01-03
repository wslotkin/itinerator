package itinerator.evaluator;

import itinerator.datamodel.Itinerary;

public class FunEvaluator implements Evaluator {
    public double evaluate(Itinerary itinerary) {
        return itinerary.getEvents().stream()
                .mapToDouble(a -> a.getActivity().getScore() * a.getActivity().getDuration())
                .sum();
    }
}
