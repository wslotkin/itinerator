package itinerator.main;

import itinerator.datamodel.Activity;
import itinerator.solver.ItinerarySolver;
import itinerator.solver.ItinerarySolver.SolverResult;
import org.joda.time.DateTime;

import java.io.IOException;
import java.util.List;

import static itinerator.solver.ItinerarySolver.createSolver;

public class OptimizerMain extends BaseMain {
    private static final double MUTATION_RATE = 0.2;
    private static final int POPULATION_SIZE = 1000;
    private static final int ITERATION_THRESHOLD = 100;
    private static final long TIMEOUT_THRESHOLD_MILLIS = Integer.MAX_VALUE;
    private static final String OUTPUT_FILENAME = "itinerary.txt";
    private static final int NUMBER_OF_THREADS = 2;

    public OptimizerMain(String outputFilename) {
        super(outputFilename, NYC_DATA);
    }

    public static void main(String[] args) throws IOException {
        new OptimizerMain(OUTPUT_FILENAME).run();
    }

    @Override
    protected SolverResult getResult(List<Activity> activities, DateTime startTime, DateTime endTime) throws IOException {
        ItinerarySolver itinerarySolver = createSolver(activities,
                startTime,
                endTime,
                POPULATION_SIZE,
                MUTATION_RATE,
                ITERATION_THRESHOLD,
                TIMEOUT_THRESHOLD_MILLIS,
                NUMBER_OF_THREADS);
        itinerarySolver.run();
        return itinerarySolver.getBestResult();
    }
}
