package itinerator.evaluator;

import cz.cvut.felk.cig.jcop.problem.BaseFitness;
import cz.cvut.felk.cig.jcop.problem.Configuration;
import itinerator.datamodel.Activity;

import java.util.List;

public class ItineraryFitness extends BaseFitness {

    private final ItineraryEvaluator evaluator;
    private final ItineraryFactory itineraryFactory;

    public ItineraryFitness(List<Activity> activities, ItineraryEvaluator evaluator, ItineraryFactory itineraryFactory) {
        this.evaluator = evaluator;
        this.itineraryFactory = itineraryFactory;
        maxFitness = activities.stream().mapToDouble(Activity::getScore).sum();
        minFitness = 0.0;
    }

    @Override
    public double getValue(Configuration configuration) {
        return evaluator.evaluate(itineraryFactory.create(configuration));
    }
}
