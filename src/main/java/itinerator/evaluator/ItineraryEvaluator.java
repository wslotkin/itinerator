package itinerator.evaluator;

import itinerator.datamodel.Itinerary;

public class ItineraryEvaluator {

    public double evaluate(Itinerary itinerary) {
        return totalFun(itinerary);
    }

    private double totalFun(Itinerary itinerary) {
        return itinerary.getEvents().stream()
                .mapToDouble(a -> a.getActivity().getScore() * a.getActivity().getDuration())
                .sum();
    }
}
