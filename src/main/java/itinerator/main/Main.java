package itinerator.main;

import cz.cvut.felk.cig.jcop.algorithm.geneticalgorithm.GeneticAlgorithm;
import cz.cvut.felk.cig.jcop.solver.SimpleSolver;
import cz.cvut.felk.cig.jcop.solver.condition.IterationCondition;
import itinerator.calculators.DistanceCalculator;
import itinerator.calculators.TravelTimeCalculator;
import itinerator.data.DataLoader;
import itinerator.datamodel.Activity;
import itinerator.itinerary.ItineraryFactory;
import itinerator.solver.ItineraryProblem;
import itinerator.solver.ItinerarySolver;
import itinerator.solver.ItinerarySolver.SolverResult;
import org.joda.time.DateTime;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static itinerator.itinerary.ItineraryFormatter.prettyPrint;
import static java.lang.String.format;

public class Main {
    private static final String[] DATA_FILES = {"beijingspots.csv", "beijingrestaurants.csv"};
    private static final double MUTATION_RATE = 0.2;
    private static final int POPULATION_SIZE = 1000;
    private static final int ITERATION_THRESHOLD = 100;
    private static final int NUMBER_OF_DAYS = 2;

    public static void main(String[] args) throws IOException {
        List<Activity> activities = loadActivities();

        DateTime startTime = new DateTime().minusDays(NUMBER_OF_DAYS);
        DateTime endTime = new DateTime();

        ItinerarySolver itinerarySolver = createItinerarySolver(activities, startTime, endTime);

        long runStartTime = System.currentTimeMillis();
        itinerarySolver.run();
        long optimizationDuration = System.currentTimeMillis() - runStartTime;

        printBestResult(optimizationDuration, itinerarySolver.getBestResult());
    }

    private static List<Activity> loadActivities() throws IOException {
        List<Activity> activities = new ArrayList<>();
        DataLoader dataLoader = new DataLoader();
        for (String dataFile : DATA_FILES) {
            URL dataFileUrl = Main.class.getClassLoader().getResource(dataFile);
            if (dataFileUrl != null) {
                activities.addAll(dataLoader.loadData(dataFileUrl.getPath()));
            }
        }
        return activities;
    }

    private static ItinerarySolver createItinerarySolver(List<Activity> activities, DateTime startTime, DateTime endTime) {
        TravelTimeCalculator travelTimeCalculator = new TravelTimeCalculator(new DistanceCalculator());
        ItineraryFactory itineraryFactory = new ItineraryFactory(activities, startTime, endTime, travelTimeCalculator);
        ItineraryProblem itineraryProblem = new ItineraryProblem(activities, startTime, endTime, itineraryFactory);
        SimpleSolver solver = new SimpleSolver(new GeneticAlgorithm(POPULATION_SIZE, MUTATION_RATE), itineraryProblem);
        solver.addStopCondition(new IterationCondition(ITERATION_THRESHOLD));
        return new ItinerarySolver(solver, itineraryProblem, itineraryFactory);
    }

    private static void printBestResult(long optimizationDuration, SolverResult bestResult) {
        String outputString = "Optimization complete. Total time: %dms. Best itinerary with score %.2f:";
        System.out.println(format(outputString, optimizationDuration, bestResult.getScore()));
        System.out.println(prettyPrint(bestResult.getItinerary()));
    }
}
