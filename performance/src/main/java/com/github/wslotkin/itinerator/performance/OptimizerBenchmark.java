package com.github.wslotkin.itinerator.performance;

import com.github.wslotkin.itinerator.generator.config.ImmutableGeneticAlgorithmConfig;
import com.github.wslotkin.itinerator.generator.config.ImmutableOptimizationConfig;
import com.github.wslotkin.itinerator.generator.main.OptimizerMain;
import com.github.wslotkin.itinerator.generator.solver.ItinerarySolver;
import org.openjdk.jmh.annotations.Benchmark;

import java.io.IOException;

public class OptimizerBenchmark {

    // See benchmarkResults.txt
    @Benchmark
    public ItinerarySolver.SolverResult runOptimizer() throws IOException {
        return new OptimizerMain(ImmutableOptimizationConfig.builder()
                .geneticAlgorithmConfig(ImmutableGeneticAlgorithmConfig.builder()
                        .populationSize(100)
                        .maxIterations(50)
                        .build())
                .build()).run();
    }
}
