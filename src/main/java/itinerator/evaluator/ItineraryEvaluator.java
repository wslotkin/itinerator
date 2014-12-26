package itinerator.evaluator;

import itinerator.datamodel.Itinerary;

public class ItineraryEvaluator {

    public double evaluate(Itinerary itinerary) {
        double sum = itinerary.getEvents().stream()
                .mapToDouble(a -> a.getActivity().getScore() * a.getActivity().getDuration())
                .sum();
        return sum;
    }
}
