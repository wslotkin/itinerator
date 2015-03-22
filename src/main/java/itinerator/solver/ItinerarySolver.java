package itinerator.solver;

import cz.cvut.felk.cig.jcop.algorithm.geneticalgorithm.GeneticAlgorithm;
import cz.cvut.felk.cig.jcop.problem.Configuration;
import cz.cvut.felk.cig.jcop.result.ResultEntry;
import cz.cvut.felk.cig.jcop.solver.SimpleSolver;
import cz.cvut.felk.cig.jcop.solver.Solver;
import cz.cvut.felk.cig.jcop.solver.condition.IterationCondition;
import cz.cvut.felk.cig.jcop.solver.condition.TimeoutCondition;
import itinerator.calculators.DistanceCalculator;
import itinerator.calculators.TravelTimeCalculator;
import itinerator.datamodel.Activity;
import itinerator.datamodel.Event;
import itinerator.datamodel.Itinerary;
import itinerator.itinerary.ItineraryFactory;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.collect.Iterables.getOnlyElement;

public class ItinerarySolver {

    private final Solver solver;
    private final ItineraryFactory itineraryFactory;

    public static ItinerarySolver createSolver(List<Activity> activities,
                                               DateTime startTime,
                                               DateTime endTime,
                                               int populationSize,
                                               double mutationRate,
                                               int iterationThreshold,
                                               long timeoutThresholdMillis) {
        TravelTimeCalculator travelTimeCalculator = new TravelTimeCalculator(new DistanceCalculator());
        ItineraryFactory itineraryFactory = new ItineraryFactory(activities, startTime, endTime, travelTimeCalculator, new ArrayList<>());
        ItineraryProblem itineraryProblem = new ItineraryProblem(activities, startTime, endTime, itineraryFactory);
        SimpleSolver solver = new SimpleSolver(new GeneticAlgorithm(populationSize, mutationRate), itineraryProblem);
        solver.addStopCondition(new IterationCondition(iterationThreshold));
        solver.addStopCondition(new TimeoutCondition(timeoutThresholdMillis));
        return new ItinerarySolver(solver, itineraryFactory);
    }

    public static SolverResult generateResult(List<Activity> activities,
                                              DateTime startTime,
                                              DateTime endTime,
                                              List<Event> events) {
        TravelTimeCalculator travelTimeCalculator = new TravelTimeCalculator(new DistanceCalculator());
        ItineraryFactory itineraryFactory = new ItineraryFactory(activities, startTime, endTime, travelTimeCalculator, events);
        ItineraryProblem itineraryProblem = new ItineraryProblem(activities, startTime, endTime, itineraryFactory);
        List<Integer> attributes = activities.stream().map(activities::indexOf).collect(Collectors.toList());

        Configuration configuration = new Configuration(attributes);
        Itinerary itinerary = itineraryFactory.create(configuration);
        double score = itineraryProblem.getDefaultFitness().getValue(configuration);
        return new SolverResult(itinerary, configuration, score, 0L);
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
        private final double score;
        private final long duration;

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
