package itinerator.main;

import itinerator.config.OptimizationConfig;
import itinerator.datamodel.Activity;
import itinerator.solver.ItinerarySolver;
import itinerator.solver.ItinerarySolver.SolverResult;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static itinerator.solver.ItinerarySolver.createSolver;

public class OptimizerMain extends BaseMain {
    private final OptimizationConfig optimizationConfig;

    public static void main(String[] args) throws IOException {
        new OptimizerMain(new OptimizationConfig.Builder().build()).run();
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
