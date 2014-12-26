package itinerator.evaluator;

import cz.cvut.felk.cig.jcop.problem.Configuration;
import cz.cvut.felk.cig.jcop.problem.Fitness;

public class ItineraryFitness implements Fitness {

    @Override
    public double getValue(Configuration configuration) {
        return 0;
    }

    @Override
    public double normalize(double fitness) throws IllegalStateException {
        return 0;
    }
}
