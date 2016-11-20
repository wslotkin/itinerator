package itinerator;

import itinerator.config.ImmutableGeneticAlgorithmConfig;
import itinerator.config.ImmutableOptimizationConfig;
import itinerator.main.OptimizerMain;
import itinerator.solver.ItinerarySolver;
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
