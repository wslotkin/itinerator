package itinerator.solver;

import cz.cvut.felk.cig.jcop.algorithm.geneticalgorithm.GeneticAlgorithm;
import cz.cvut.felk.cig.jcop.problem.Configuration;
import cz.cvut.felk.cig.jcop.result.ResultEntry;
import cz.cvut.felk.cig.jcop.solver.SimpleSolver;
import cz.cvut.felk.cig.jcop.solver.Solver;
import cz.cvut.felk.cig.jcop.solver.condition.IterationCondition;
import itinerator.calculators.DistanceCalculator;
import itinerator.calculators.TravelTimeCalculator;
import itinerator.datamodel.Activity;
import itinerator.datamodel.Itinerary;
import itinerator.itinerary.ItineraryFactory;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Iterables.getOnlyElement;

public class ItinerarySolver {

    private final Solver solver;
    private final ItineraryFactory itineraryFactory;

    public static ItinerarySolver createSolver(List<Activity> activities,
                                               DateTime startTime,
                                               DateTime endTime,
                                               int populationSize,
                                               double mutationRate,
                                               int iterationThreshold) {
        TravelTimeCalculator travelTimeCalculator = new TravelTimeCalculator(new DistanceCalculator());
        ItineraryFactory itineraryFactory = new ItineraryFactory(activities, startTime, endTime, travelTimeCalculator, new ArrayList<>());
        ItineraryProblem itineraryProblem = new ItineraryProblem(activities, startTime, endTime, itineraryFactory);
        SimpleSolver solver = new SimpleSolver(new GeneticAlgorithm(populationSize, mutationRate), itineraryProblem);
        solver.addStopCondition(new IterationCondition(iterationThreshold));
        return new ItinerarySolver(solver, itineraryFactory);
    }

    public ItinerarySolver(Solver solver, ItineraryFactory itineraryFactory) {
        this.solver = solver;
        this.itineraryFactory = itineraryFactory;
    }

    public void run() {
        solver.run();
    }

    public SolverResult getBestResult() {
        ResultEntry onlyResult = getOnlyElement(solver.getResult().getResultEntries());
        Configuration bestConfiguration = onlyResult.getBestConfiguration();
        Itinerary bestItinerary = itineraryFactory.create(bestConfiguration);
        double score = onlyResult.getBestFitness();
        long duration = onlyResult.getStopTimestamp().getClockTime() - onlyResult.getStartTimestamp().getClockTime();
        return new SolverResult(bestItinerary, bestConfiguration, score, duration);
    }

    public static class SolverResult {
        private final Itinerary itinerary;
        private final Configuration configuration;
        private final long duration;
        private final double score;

        public SolverResult(Itinerary itinerary, Configuration configuration, double score, long duration) {
            this.itinerary = itinerary;
            this.score = score;
            this.configuration = configuration;
            this.duration = duration;
        }

        public Itinerary getItinerary() {
            return itinerary;
        }

        public Configuration getConfiguration() {
            return configuration;
        }

        public double getScore() {
            return score;
        }

        public long getDuration() {
            return duration;
        }
    }
}
