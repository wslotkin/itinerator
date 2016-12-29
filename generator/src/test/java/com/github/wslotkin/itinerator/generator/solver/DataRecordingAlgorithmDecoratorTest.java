package com.github.wslotkin.itinerator.generator.solver;

import com.github.wslotkin.itinerator.generator.evaluator.EvaluatorState;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import cz.cvut.felk.cig.jcop.algorithm.Algorithm;
import cz.cvut.felk.cig.jcop.problem.Configuration;
import cz.cvut.felk.cig.jcop.problem.Fitness;
import cz.cvut.felk.cig.jcop.problem.ObjectiveProblem;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import java.io.BufferedWriter;
import java.io.IOException;

import static com.github.wslotkin.itinerator.generator.TestUtil.DELTA;
import static com.github.wslotkin.itinerator.generator.evaluator.EvaluatorType.FUN;
import static com.github.wslotkin.itinerator.generator.evaluator.EvaluatorType.TOTAL;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class DataRecordingAlgorithmDecoratorTest {
    private static final double BEST_FITNESS = 1.2;
    private static final double FUN_SCORE = 2.0;
    private static final double TOTAL_SCORE = 3.0;
    private static final String LABEL = "label";
    private static final Configuration BEST_CONFIGURATION = new Configuration(ImmutableList.of(1, 2, 3));
    private static final Fitness FITNESS = mock(Fitness.class);
    private static final ObjectiveProblem OBJECTIVE_PROBLEM = mock(ObjectiveProblem.class);

    private Algorithm algorithm;
    private EvaluatorState evaluatorState;
    private ItineraryFitness itineraryFitness;
    private BufferedWriter bufferedWriter;
    private DataRecordingAlgorithmDecorator dataRecordingAlgorithm;

    @Before
    public void before() throws IOException {
        algorithm = mock(Algorithm.class);
        evaluatorState = mock(EvaluatorState.class);
        itineraryFitness = mock(ItineraryFitness.class);
        bufferedWriter = mock(BufferedWriter.class);

        when(algorithm.getBestConfiguration()).thenReturn(BEST_CONFIGURATION);
        when(algorithm.getLabel()).thenReturn(LABEL);
        when(algorithm.getBestFitness()).thenReturn(BEST_FITNESS);
        when(evaluatorState.getScoreByEvaluator()).thenReturn(ImmutableMap.of(FUN, FUN_SCORE, TOTAL, TOTAL_SCORE));

        dataRecordingAlgorithm = new DataRecordingAlgorithmDecorator(algorithm, evaluatorState, itineraryFitness, bufferedWriter);

        verify(bufferedWriter).write(isA(String.class));
    }

    @Test
    public void shouldRecordEvaluatorStateAfterEachOptimization() throws IOException {
        dataRecordingAlgorithm.optimize();

        InOrder inOrder = inOrder(algorithm, itineraryFitness, evaluatorState, bufferedWriter);
        inOrder.verify(algorithm).optimize();
        inOrder.verify(evaluatorState).clear();
        inOrder.verify(itineraryFitness).getValue(BEST_CONFIGURATION);
        inOrder.verify(evaluatorState).getScoreByEvaluator();
        inOrder.verify(bufferedWriter).write(FUN_SCORE + "," + TOTAL_SCORE);
    }

    @Test
    public void shouldDelegateAllOtherMethodsToWrappedAlgorithm() {
        dataRecordingAlgorithm.init(OBJECTIVE_PROBLEM);
        verify(algorithm).init(OBJECTIVE_PROBLEM);

        assertEquals(BEST_CONFIGURATION, dataRecordingAlgorithm.getBestConfiguration());

        dataRecordingAlgorithm.cleanUp();
        verify(algorithm).cleanUp();

        assertEquals(LABEL, dataRecordingAlgorithm.getLabel());

        assertEquals(BEST_FITNESS, dataRecordingAlgorithm.getBestFitness(), DELTA);

        dataRecordingAlgorithm.setFitness(FITNESS);
        verify(algorithm).setFitness(FITNESS);

        dataRecordingAlgorithm.setLabel(LABEL);
        verify(algorithm).setLabel(LABEL);
    }
}