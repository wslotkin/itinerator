package com.github.wslotkin.itinerator.performance;

import com.github.wslotkin.itinerator.generator.config.ImmutableGeneticAlgorithmConfig;
import com.github.wslotkin.itinerator.generator.config.ImmutableOptimizationConfig;
import com.github.wslotkin.itinerator.generator.datamodel.SolverResult;
import org.openjdk.jmh.annotations.Benchmark;

import java.io.IOException;

import static com.github.wslotkin.itinerator.generator.main.OptimizerMain.runOptimization;

public class OptimizerBenchmark {

    // See benchmarkResults.txt
    @Benchmark
    public SolverResult runOptimizer() throws IOException {
        return runOptimization(ImmutableOptimizationConfig.builder()
                .geneticAlgorithmConfig(ImmutableGeneticAlgorithmConfig.builder()
                        .populationSize(100)
                        .maxIterations(50)
                        .build())
                .build());
    }
}
