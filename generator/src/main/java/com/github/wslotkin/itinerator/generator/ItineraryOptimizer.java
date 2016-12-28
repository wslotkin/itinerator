package com.github.wslotkin.itinerator.generator;

import com.github.wslotkin.itinerator.generator.config.OptimizationConfig;
import com.github.wslotkin.itinerator.generator.datamodel.Activity;
import com.github.wslotkin.itinerator.generator.datamodel.Event;
import com.github.wslotkin.itinerator.generator.datamodel.SolverResult;
import com.github.wslotkin.itinerator.generator.solver.ItinerarySolver;

import java.time.LocalDateTime;
import java.util.List;

import static com.github.wslotkin.itinerator.generator.itinerary.ItineraryFormatter.prettyPrint;
import static com.github.wslotkin.itinerator.generator.solver.ItinerarySolver.createSolver;

public class ItineraryOptimizer implements ItineraryGenerator {
    private final OptimizationConfig optimizationConfig;

    public ItineraryOptimizer(OptimizationConfig optimizationConfig) {
        this.optimizationConfig = optimizationConfig;
    }

    @Override
    public SolverResult getResult(List<Activity> activities,
                                  List<Event> fixedEvents,
                                  LocalDateTime startTime,
                                  LocalDateTime endTime) {
        System.out.println("Input configuration: " + optimizationConfig);
        System.out.println("Input activities: " + activities);

        ItinerarySolver itinerarySolver = createSolver(activities,
                fixedEvents,
                startTime,
                endTime,
                optimizationConfig.getGeneticAlgorithmConfig(),
                optimizationConfig.getEvaluationConfig());
        itinerarySolver.run();

        SolverResult bestResult = itinerarySolver.getBestResult();
        System.out.println("Output itinerary: " + prettyPrint(bestResult.getItinerary()));
        return bestResult;
    }
}
