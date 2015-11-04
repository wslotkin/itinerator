package itinerator;

import itinerator.config.GeneticAlgorithmConfig;
import itinerator.config.OptimizationConfig;
import itinerator.main.OptimizerMain;
import itinerator.solver.ItinerarySolver;
import org.openjdk.jmh.annotations.Benchmark;

import java.io.IOException;

public class OptimizerBenchmark {

//  11/3/15, commit a7e2331
//  Result "runOptimizer":
//  4.849 ±(99.9%) 0.154 ops/s [Average]
//  (min, avg, max) = (1.984, 4.849, 6.041), stdev = 0.653
//  CI (99.9%): [4.695, 5.003] (assumes normal distribution)
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
