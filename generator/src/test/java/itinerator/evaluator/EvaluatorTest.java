package itinerator.evaluator;

import org.junit.Test;

import static itinerator.TestUtil.DELTA;
import static org.junit.Assert.assertEquals;

public class EvaluatorTest {
    private static final double UNUSED = 0.0;
    private static final double RESULT_1 = 1.0;
    private static final double RESULT_2 = 2.0;
    private static final Evaluator<Double> EVALUATOR_1 = x -> RESULT_1;
    private static final Evaluator<Double> EVALUATOR_2 = x -> RESULT_2;

    @Test
    public void andThenMethodShouldChainMultipleEvaluators() {
        Evaluator<Double> compositeEvaluator = EVALUATOR_1.andThen(EVALUATOR_2);

        assertEquals(RESULT_1, EVALUATOR_1.applyAsDouble(UNUSED), DELTA);
        assertEquals(RESULT_2, EVALUATOR_2.applyAsDouble(UNUSED), DELTA);
        assertEquals(RESULT_1 + RESULT_2, compositeEvaluator.applyAsDouble(UNUSED), DELTA);
    }
}