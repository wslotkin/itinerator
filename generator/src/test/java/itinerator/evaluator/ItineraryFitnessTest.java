package itinerator.evaluator;

import cz.cvut.felk.cig.jcop.problem.Configuration;
import itinerator.datamodel.Itinerary;
import itinerator.itinerary.ItineraryFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static itinerator.TestConstants.DELTA;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ItineraryFitnessTest {

    private static final Configuration CONFIGURATION = new Configuration(new ArrayList<>());
    private static final Itinerary ITINERARY = new Itinerary(new ArrayList<>());
    private static final double FITNESS_SCORE = 1.2;

    private CompositeEvaluator evaluator;
    private ItineraryFactory factory;

    private ItineraryFitness itineraryFitness;

    @Before
    public void before() {
        evaluator = mock(CompositeEvaluator.class);
        factory = mock(ItineraryFactory.class);

        when(factory.create(CONFIGURATION)).thenReturn(ITINERARY);
        when(evaluator.evaluate(ITINERARY)).thenReturn(FITNESS_SCORE);

        itineraryFitness = new ItineraryFitness(evaluator, factory);
    }

    @Test
    public void callsEvaluateWithConvertedItinerary() {
        double value = itineraryFitness.getValue(CONFIGURATION);

        assertEquals(FITNESS_SCORE, value, DELTA);

        verify(factory).create(CONFIGURATION);
        verify(evaluator).evaluate(ITINERARY);
    }
}