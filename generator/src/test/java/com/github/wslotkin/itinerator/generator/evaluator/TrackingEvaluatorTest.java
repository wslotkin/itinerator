package com.github.wslotkin.itinerator.generator.evaluator;

import org.junit.Before;
import org.junit.Test;

import static com.github.wslotkin.itinerator.generator.TestUtil.DELTA;
import static com.github.wslotkin.itinerator.generator.TestUtil.mockGeneric;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class TrackingEvaluatorTest {
    private static final int INPUT = 1;
    private static final double SCORE = 2.0;

    private Evaluator<Integer> evaluator;
    private EvaluatorState evaluatorState;
    private TrackingEvaluator<Integer> trackingEvaluator;

    @Before
    public void before() {
        evaluator = mockGeneric(Evaluator.class);
        evaluatorState = mock(EvaluatorState.class);

        when(evaluator.applyAsDouble(INPUT)).thenReturn(SCORE);

        trackingEvaluator = new TrackingEvaluator<>(evaluator, evaluatorState, EvaluatorType.COST);
    }

    @Test
    public void shouldDelegateToEvaluatorAndUpdateState() {
        double result = trackingEvaluator.applyAsDouble(INPUT);

        assertEquals(SCORE, result, DELTA);

        verify(evaluator).applyAsDouble(INPUT);
        verify(evaluatorState).update(EvaluatorType.COST, SCORE);
    }
}