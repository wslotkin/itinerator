package com.github.wslotkin.itinerator.generator.solver;

import com.github.wslotkin.itinerator.generator.calculators.RoundingTravelTimeCalculator;
import com.github.wslotkin.itinerator.generator.config.EvaluationConfig;
import com.github.wslotkin.itinerator.generator.config.GeneticAlgorithmConfig;
import com.github.wslotkin.itinerator.generator.datamodel.*;
import com.github.wslotkin.itinerator.generator.itinerary.ItineraryFactory;
import cz.cvut.felk.cig.jcop.algorithm.Algorithm;
import cz.cvut.felk.cig.jcop.algorithm.geneticalgorithm.GeneticAlgorithm;
import cz.cvut.felk.cig.jcop.problem.Configuration;
import cz.cvut.felk.cig.jcop.result.ResultEntry;
import cz.cvut.felk.cig.jcop.solver.SimpleSolver;
import cz.cvut.felk.cig.jcop.solver.Solver;
import cz.cvut.felk.cig.jcop.solver.condition.IterationCondition;
import cz.cvut.felk.cig.jcop.solver.condition.TimeoutCondition;

import java.time.LocalDateTime;
import java.util.List;

import static com.github.wslotkin.itinerator.generator.calculators.RoundingTravelTimeCalculator.roundingTravelTimeCalculator;
import static com.github.wslotkin.itinerator.generator.solver.DataRecordingAlgorithmDecorator.wrapWithDataRecordingAlgorithm;
import static com.google.common.collect.Iterables.getOnlyElement;

public class ItinerarySolver {

    private final Solver solver;
    private final ItineraryFactory itineraryFactory;

    public static ItinerarySolver createSolver(List<Activity> activities,
                                               List<Event> fixedEvents,
                                               LocalDateTime startTime,
                                               LocalDateTime endTime,
                                               GeneticAlgorithmConfig geneticAlgorithmConfig,
                                               EvaluationConfig evaluationConfig) {
        RoundingTravelTimeCalculator travelTimeCalculator = roundingTravelTimeCalculator();
        ItineraryFactory itineraryFactory = new ItineraryFactory(activities, startTime, endTime, travelTimeCalculator, fixedEvents);
        ItineraryProblem itineraryProblem = new ItineraryProblem(activities, startTime, endTime, itineraryFactory, evaluationConfig);
        Algorithm algorithm = createAlgorithm(activities, geneticAlgorithmConfig, evaluationConfig, itineraryFactory);
        SimpleSolver solver = new SimpleSolver(algorithm, itineraryProblem);
        solver.addStopCondition(new IterationCondition(geneticAlgorithmConfig.getMaxIterations()));
        solver.addStopCondition(new TimeoutCondition(geneticAlgorithmConfig.getMaxDuration()));
        return new ItinerarySolver(solver, itineraryFactory);
    }

    private ItinerarySolver(Solver solver, ItineraryFactory itineraryFactory) {
        this.solver = solver;
        this.itineraryFactory = itineraryFactory;
    }

    public void run() {
        solver.run();
    }

    public SolverResult getBestResult() {
        ResultEntry onlyResult = getOnlyElement(solver.getResult().getResultEntries());
        throwIfInvalidResult(onlyResult);

        Configuration bestConfiguration = onlyResult.getBestConfiguration();
        Itinerary bestItinerary = itineraryFactory.create(bestConfiguration);
        double score = onlyResult.getBestFitness();
        long duration = onlyResult.getStopTimestamp().getClockTime() - onlyResult.getStartTimestamp().getClockTime();
        return ImmutableSolverResult.of(bestItinerary, bestConfiguration, score, duration);
    }

    private static void throwIfInvalidResult(ResultEntry resultEntry) {
        Configuration bestConfiguration = resultEntry.getBestConfiguration();
        Exception exception = resultEntry.getException();
        if (bestConfiguration == null && exception != null) {
            throw new RuntimeException(exception);
        }
    }

    private static Algorithm createAlgorithm(List<Activity> activities,
                                             GeneticAlgorithmConfig geneticAlgorithmConfig,
                                             EvaluationConfig evaluationConfig,
                                             ItineraryFactory itineraryFactory) {
        Algorithm geneticAlgorithm = createGeneticAlgorithm(geneticAlgorithmConfig.getPopulationSize(),
                geneticAlgorithmConfig.getMutationRate(),
                geneticAlgorithmConfig.getParallelized());
        if (evaluationConfig.shouldLogEvaluatorResults()) {
            return wrapWithDataRecordingAlgorithm(geneticAlgorithm,
                    evaluationConfig,
                    activities,
                    itineraryFactory,
                    "evaluatorOutput.csv");
        } else {
            return geneticAlgorithm;
        }
    }

    private static GeneticAlgorithm createGeneticAlgorithm(int populationSize, double mutationRate, boolean parallelized) {
        if (parallelized) {
            return new ParallelGeneticAlgorithm(populationSize, mutationRate);
        } else {
            return new GeneticAlgorithm(populationSize, mutationRate);
        }
    }
}
