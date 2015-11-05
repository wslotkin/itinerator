package itinerator;

import itinerator.config.GeneticAlgorithmConfig;
import itinerator.config.OptimizationConfig;
import itinerator.main.OptimizerMain;
import itinerator.solver.ItinerarySolver;
import org.openjdk.jmh.annotations.Benchmark;

import java.io.IOException;

public class OptimizerBenchmark {

    // See benchmarkResults.txt
    @Benchmark
    public ItinerarySolver.SolverResult runOptimizer() throws IOException {
        return new OptimizerMain(new OptimizationConfig.Builder()
                .setGeneticAlgorithmConfig(new GeneticAlgorithmConfig.Builder()
                        .setPopulationSize(100)
                        .setMaxIterations(50)
                        .build())
                .build()).run();
    }
}
