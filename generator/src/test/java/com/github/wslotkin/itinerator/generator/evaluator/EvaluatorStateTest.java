package com.github.wslotkin.itinerator.generator.evaluator;

import org.junit.Before;
import org.junit.Test;

import java.util.EnumSet;
import java.util.Map;

import static com.github.wslotkin.itinerator.generator.TestUtil.DELTA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EvaluatorStateTest {
    private static final double COST_SCORE = 1.0;
    private static final double FUN_SCORE_1 = 2.0;
    private static final double FUN_SCORE_2 = 3.0;
    private static final double TOTAL_SCORE = 6.0;

    private EvaluatorState evaluatorState;

    @Before
    public void before() {
        evaluatorState = new EvaluatorState();
    }

    @Test
    public void shouldContainEntryForEachEvaluatorType() {
        Map<EvaluatorType, Double> result = evaluatorState.getScoreByEvaluator();

        assertTrue(result.keySet().containsAll(EnumSet.allOf(EvaluatorType.class)));
    }

    @Test
    public void shouldAggregateAllScoresForEachType() {
        evaluatorState.update(EvaluatorType.COST, COST_SCORE);
        evaluatorState.update(EvaluatorType.FUN, FUN_SCORE_1);
        evaluatorState.update(EvaluatorType.FUN, FUN_SCORE_2);
        evaluatorState.update(EvaluatorType.TOTAL, TOTAL_SCORE);

        Map<EvaluatorType, Double> result = evaluatorState.getScoreByEvaluator();

        assertEquals(COST_SCORE, result.get(EvaluatorType.COST), DELTA);
        assertEquals(FUN_SCORE_1 + FUN_SCORE_2, result.get(EvaluatorType.FUN), DELTA);
        assertEquals(TOTAL_SCORE, result.get(EvaluatorType.TOTAL), DELTA);
    }

    @Test
    public void whenClearedShouldResetAllScores() {
        evaluatorState.update(EvaluatorType.COST, COST_SCORE);

        evaluatorState.clear();

        Map<EvaluatorType, Double> result = evaluatorState.getScoreByEvaluator();

        assertEquals(0.0, result.get(EvaluatorType.COST), DELTA);
    }
}