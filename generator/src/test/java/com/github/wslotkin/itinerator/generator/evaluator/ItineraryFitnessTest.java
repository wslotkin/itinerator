package com.github.wslotkin.itinerator.generator.evaluator;

import com.github.wslotkin.itinerator.generator.datamodel.ImmutableItinerary;
import com.github.wslotkin.itinerator.generator.datamodel.Itinerary;
import com.github.wslotkin.itinerator.generator.itinerary.ItineraryFactory;
import cz.cvut.felk.cig.jcop.problem.Configuration;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static com.github.wslotkin.itinerator.generator.performance.TestUtil.DELTA;
import static com.github.wslotkin.itinerator.generator.performance.TestUtil.mockGeneric;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ItineraryFitnessTest {

    private static final Configuration CONFIGURATION = new Configuration(new ArrayList<>());
    private static final Itinerary ITINERARY = ImmutableItinerary.of(new ArrayList<>());
    private static final double FITNESS_SCORE = 1.2;

    private Evaluator<Itinerary> evaluator;
    private ItineraryFactory factory;

    private ItineraryFitness itineraryFitness;

    @Before
    public void before() {
        evaluator = mockGeneric(Evaluator.class);
        factory = mock(ItineraryFactory.class);

        when(factory.create(CONFIGURATION)).thenReturn(ITINERARY);
        when(evaluator.applyAsDouble(ITINERARY)).thenReturn(FITNESS_SCORE);

        itineraryFitness = new ItineraryFitness(evaluator, factory);
    }

    @Test
    public void callsEvaluateWithConvertedItinerary() {
        double value = itineraryFitness.getValue(CONFIGURATION);

        assertEquals(FITNESS_SCORE, value, DELTA);

        verify(factory).create(CONFIGURATION);
        verify(evaluator).applyAsDouble(ITINERARY);
    }
}