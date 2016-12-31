package com.github.wslotkin.itinerator.generator.solver;

import com.github.wslotkin.itinerator.generator.datamodel.Itinerary;
import com.github.wslotkin.itinerator.generator.evaluator.Evaluator;
import com.github.wslotkin.itinerator.generator.itinerary.ItineraryFactory;
import cz.cvut.felk.cig.jcop.problem.Configuration;
import cz.cvut.felk.cig.jcop.problem.Fitness;

public class ItineraryFitness implements Fitness {

    private final Evaluator<Itinerary> evaluator;
    private final ItineraryFactory itineraryFactory;

    public ItineraryFitness(Evaluator<Itinerary> evaluator, ItineraryFactory itineraryFactory) {
        this.evaluator = evaluator;
        this.itineraryFactory = itineraryFactory;
    }

    @Override
    public double getValue(Configuration configuration) {
        return getValue(itineraryFactory.create(configuration));
    }

    public double getValue(Itinerary itinerary) {
        return evaluator.applyAsDouble(itinerary);
    }

    @Override
    public double normalize(double fitness) throws IllegalStateException {
        throw new IllegalStateException("Unexpected call to normalize().");
    }
}
