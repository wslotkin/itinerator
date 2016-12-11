package com.github.wslotkin.itinerator.generator.main;

import com.github.wslotkin.itinerator.generator.config.ImmutableOptimizationConfig;
import com.github.wslotkin.itinerator.generator.config.OptimizationConfig;
import com.github.wslotkin.itinerator.generator.datamodel.Activity;
import com.github.wslotkin.itinerator.generator.solver.ItinerarySolver;
import com.github.wslotkin.itinerator.generator.solver.ItinerarySolver.SolverResult;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static com.github.wslotkin.itinerator.generator.solver.ItinerarySolver.createSolver;

public class OptimizerMain extends BaseMain {
    private final OptimizationConfig optimizationConfig;

    public static void main(String[] args) throws IOException {
        new OptimizerMain(ImmutableOptimizationConfig.builder().build()).run();
    }

    public OptimizerMain(OptimizationConfig optimizationConfig) {
        super(optimizationConfig.getItineratorConfig());
        this.optimizationConfig = optimizationConfig;
    }

    @Override
    protected SolverResult getResult(List<Activity> activities, LocalDateTime startTime, LocalDateTime endTime) throws IOException {
        ItinerarySolver itinerarySolver = createSolver(activities,
                startTime,
                endTime,
                optimizationConfig.getGeneticAlgorithmConfig(),
                optimizationConfig.getEvaluationConfig());
        itinerarySolver.run();
        return itinerarySolver.getBestResult();
    }
}
