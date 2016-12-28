package com.github.wslotkin.itinerator.generator.solver;

import com.github.wslotkin.itinerator.generator.calculators.DistanceCalculator;
import com.github.wslotkin.itinerator.generator.calculators.RoundingTravelTimeCalculator;
import com.github.wslotkin.itinerator.generator.calculators.TravelTimeCalculator;
import com.github.wslotkin.itinerator.generator.config.EvaluationConfig;
import com.github.wslotkin.itinerator.generator.config.GeneticAlgorithmConfig;
import com.github.wslotkin.itinerator.generator.datamodel.*;
import com.github.wslotkin.itinerator.generator.itinerary.ItineraryFactory;
import cz.cvut.felk.cig.jcop.algorithm.geneticalgorithm.GeneticAlgorithm;
import cz.cvut.felk.cig.jcop.problem.Configuration;
import cz.cvut.felk.cig.jcop.result.ResultEntry;
import cz.cvut.felk.cig.jcop.solver.SimpleSolver;
import cz.cvut.felk.cig.jcop.solver.Solver;
import cz.cvut.felk.cig.jcop.solver.condition.IterationCondition;
import cz.cvut.felk.cig.jcop.solver.condition.TimeoutCondition;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.collect.Iterables.getOnlyElement;

public class ItinerarySolver {

    private final Solver solver;
    private final ItineraryFactory itineraryFactory;

    public static ItinerarySolver createSolver(List<Activity> activities,
                                               LocalDateTime startTime,
                                               LocalDateTime endTime,
                                               GeneticAlgorithmConfig geneticAlgorithmConfig,
                                               EvaluationConfig evaluationConfig) {
        RoundingTravelTimeCalculator travelTimeCalculator = new RoundingTravelTimeCalculator(new TravelTimeCalculator(new DistanceCalculator()), 5.0);
        ItineraryFactory itineraryFactory = new ItineraryFactory(activities, startTime, endTime, travelTimeCalculator, new ArrayList<>());
        ItineraryProblem itineraryProblem = new ItineraryProblem(activities, startTime, endTime, itineraryFactory, evaluationConfig);
        GeneticAlgorithm geneticAlgorithm = createGeneticAlgorithm(geneticAlgorithmConfig.getPopulationSize(),
                geneticAlgorithmConfig.getMutationRate(),
                geneticAlgorithmConfig.getParallelized());
        SimpleSolver solver = new SimpleSolver(geneticAlgorithm, itineraryProblem);
        solver.addStopCondition(new IterationCondition(geneticAlgorithmConfig.getMaxIterations()));
        solver.addStopCondition(new TimeoutCondition(geneticAlgorithmConfig.getMaxDuration()));
        return new ItinerarySolver(solver, itineraryFactory);
    }

    private static GeneticAlgorithm createGeneticAlgorithm(int populationSize, double mutationRate, boolean parallelized) {
        if (parallelized) {
            return new ParallelGeneticAlgorithm(populationSize, mutationRate);
        } else {
            return new GeneticAlgorithm(populationSize, mutationRate);
        }
    }

    public static SolverResult generateResult(List<Activity> activities,
                                              LocalDateTime startTime,
                                              LocalDateTime endTime,
                                              List<Event> events,
                                              EvaluationConfig evaluationConfig) {
        RoundingTravelTimeCalculator travelTimeCalculator = new RoundingTravelTimeCalculator(new TravelTimeCalculator(new DistanceCalculator()), 5.0);
        ItineraryFactory itineraryFactory = new ItineraryFactory(activities, startTime, endTime, travelTimeCalculator, events);
        ItineraryProblem itineraryProblem = new ItineraryProblem(activities, startTime, endTime, itineraryFactory, evaluationConfig);
        List<Integer> attributes = activities.stream().map(activities::indexOf).collect(Collectors.toList());

        Configuration configuration = new Configuration(attributes);
        Itinerary itinerary = itineraryFactory.create(configuration);
        double score = itineraryProblem.getDefaultFitness().getValue(configuration);
        return ImmutableSolverResult.of(itinerary, configuration, score, 0L);
    }

    public ItinerarySolver(Solver solver, ItineraryFactory itineraryFactory) {
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
}
