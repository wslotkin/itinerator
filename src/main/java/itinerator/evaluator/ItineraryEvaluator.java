package itinerator.evaluator;

import itinerator.datamodel.Itinerary;

import java.util.ArrayList;
import java.util.List;

public class ItineraryEvaluator {

    private List<Evaluator> evaluators;

    public ItineraryEvaluator() {
        evaluators = new ArrayList();
        evaluators.add(new FunEvaluator());
        evaluators.add(new TravelEvaluator());
    }

    public double evaluate(Itinerary itinerary) {
        return evaluators.stream()
                .mapToDouble(a -> a.evaluate(itinerary))
                .sum();
    }
}
