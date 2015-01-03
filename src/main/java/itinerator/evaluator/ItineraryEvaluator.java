package itinerator.evaluator;

import itinerator.datamodel.Itinerary;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class ItineraryEvaluator implements Evaluator {

    private final List<Evaluator> evaluators;

    public ItineraryEvaluator(Evaluator... evaluators) {
        this.evaluators = newArrayList(evaluators);
    }

    public double evaluate(Itinerary itinerary) {
        return evaluators.stream()
                .mapToDouble(a -> a.evaluate(itinerary))
                .sum();
    }
}
