package itinerator.evaluator;

import cz.cvut.felk.cig.jcop.problem.Configuration;
import cz.cvut.felk.cig.jcop.problem.Fitness;
import itinerator.itinerary.ItineraryFactory;

public class ItineraryFitness implements Fitness {

    private final Evaluator evaluator;
    private final ItineraryFactory itineraryFactory;

    public ItineraryFitness(Evaluator evaluator, ItineraryFactory itineraryFactory) {
        this.evaluator = evaluator;
        this.itineraryFactory = itineraryFactory;
    }

    @Override
    public double getValue(Configuration configuration) {
        return evaluator.evaluate(itineraryFactory.create(configuration));
    }

    @Override
    public double normalize(double fitness) throws IllegalStateException {
        throw new IllegalStateException("Unexpected call to normalize().");
    }
}
