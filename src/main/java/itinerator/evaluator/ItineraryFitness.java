package itinerator.evaluator;

import cz.cvut.felk.cig.jcop.problem.Configuration;
import cz.cvut.felk.cig.jcop.problem.Fitness;

/**
 * Created by smatt989 (matthew slotkin --> not william) on 12/26/14.
 */
public class ItineraryFitness implements Fitness{

    @Override
    public double getValue(Configuration configuration) {
        return 0;
    }

    @Override
    public double normalize(double fitness) throws IllegalStateException {
        return 0;
    }
}
