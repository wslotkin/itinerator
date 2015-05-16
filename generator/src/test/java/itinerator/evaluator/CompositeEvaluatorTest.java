package itinerator.evaluator;

import itinerator.datamodel.Itinerary;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static itinerator.TestConstants.DELTA;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CompositeEvaluatorTest {

    private static final Itinerary ITINERARY = new Itinerary(new ArrayList<>());
    private static final double FIRST_SCORE = 1.2;
    private static final double SECOND_SCORE = 2.3;

    private Evaluator firstEvaluator;
    private Evaluator secondEvaluator;

    @Before
    public void before() {
        firstEvaluator = mock(Evaluator.class);
        secondEvaluator = mock(Evaluator.class);

        when(firstEvaluator.evaluate(ITINERARY)).thenReturn(FIRST_SCORE);
        when(secondEvaluator.evaluate(ITINERARY)).thenReturn(SECOND_SCORE);
    }

    @Test
    public void whenNoEvaluatorsShouldReturnZero() {
        CompositeEvaluator compositeEvaluator = new CompositeEvaluator();
        double result = compositeEvaluator.evaluate(ITINERARY);

        assertEquals(0.0, result, DELTA);
    }

    @Test
    public void whenOneEvaluatorShouldReturnResultOfEvaluator() {
        CompositeEvaluator compositeEvaluator = new CompositeEvaluator(firstEvaluator);
        double result = compositeEvaluator.evaluate(ITINERARY);

        assertEquals(FIRST_SCORE, result, DELTA);
    }

    @Test
    public void whenMultipleEvaluatorShouldReturnSumOfResultsOfEvaluators() {
        CompositeEvaluator compositeEvaluator = new CompositeEvaluator(firstEvaluator, secondEvaluator);
        double result = compositeEvaluator.evaluate(ITINERARY);

        assertEquals(FIRST_SCORE + SECOND_SCORE, result, DELTA);
    }
}