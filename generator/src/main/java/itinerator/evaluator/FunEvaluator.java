package itinerator.evaluator;

import itinerator.datamodel.Itinerary;

public class FunEvaluator implements Evaluator {
    public double evaluate(Itinerary itinerary) {
        return itinerary.getEvents().stream()
                .mapToDouble(event -> event.getActivity().getScore() * event.getActivity().getDuration())
                .sum();
    }
}
