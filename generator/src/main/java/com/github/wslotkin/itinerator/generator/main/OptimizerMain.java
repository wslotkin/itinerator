package com.github.wslotkin.itinerator.generator.main;

import com.github.wslotkin.itinerator.generator.ItineraryOptimizer;
import com.github.wslotkin.itinerator.generator.config.ImmutableOptimizationConfig;
import com.github.wslotkin.itinerator.generator.config.OptimizationConfig;
import com.github.wslotkin.itinerator.generator.datamodel.SolverResult;

import java.io.FileNotFoundException;
import java.io.IOException;

import static com.github.wslotkin.itinerator.generator.main.FileBasedItineraryGeneratorRunner.BEIJING_DATA;

public class OptimizerMain {

    public static void main(String[] args) throws IOException {
        OptimizationConfig optimizationConfig = ImmutableOptimizationConfig.builder().build();
        runOptimization(optimizationConfig);
    }

    public static SolverResult runOptimization(OptimizationConfig optimizationConfig) throws FileNotFoundException {
        return new FileBasedItineraryGeneratorRunner(optimizationConfig.getItineratorConfig(),
                new ItineraryOptimizer(optimizationConfig),
                BEIJING_DATA,
                "output.txt").run();
    }
}
