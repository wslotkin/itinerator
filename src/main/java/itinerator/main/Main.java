package itinerator.main;

import cz.cvut.felk.cig.jcop.algorithm.geneticalgorithm.GeneticAlgorithm;
import cz.cvut.felk.cig.jcop.problem.Configuration;
import cz.cvut.felk.cig.jcop.solver.SimpleSolver;
import cz.cvut.felk.cig.jcop.solver.condition.IterationCondition;
import itinerator.calculators.DistanceCalculator;
import itinerator.calculators.TravelTimeCalculator;
import itinerator.data.DataLoader;
import itinerator.datamodel.Activity;
import itinerator.datamodel.Itinerary;
import itinerator.itinerary.ItineraryFactory;
import itinerator.itinerary.ItineraryFormatter;
import itinerator.problem.ItineraryProblem;
import org.joda.time.DateTime;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Iterables.getOnlyElement;

public class Main {

    private static final String[] DATA_FILES = {"beijingspots.csv", "beijingrestaurants.csv"};

    public static void main(String[] args) throws IOException {
        List<Activity> activities = new ArrayList<>();
        DataLoader dataLoader = new DataLoader();
        for (String dataFile : DATA_FILES) {
            URL dataFileUrl = Main.class.getClassLoader().getResource(dataFile);
            if (dataFileUrl != null) {
                activities.addAll(dataLoader.loadData(dataFileUrl.getPath()));
            }
        }

        DateTime startTime = new DateTime().minusDays(2);
        DateTime endTime = new DateTime();

        SimpleSolver solver = new SimpleSolver(new GeneticAlgorithm(1000, 0.2), new ItineraryProblem(activities, startTime, endTime));
        solver.addStopCondition(new IterationCondition(100));

        System.out.println("Starting optimizer...");
        long runStartTime = System.currentTimeMillis();
        solver.run();
        long optimizationDuration = System.currentTimeMillis() - runStartTime;

        Configuration bestConfiguration = getOnlyElement(solver.getResult().getResultEntries()).getBestConfiguration();
        Itinerary bestItinerary = new ItineraryFactory(activities, startTime, endTime, new TravelTimeCalculator(new DistanceCalculator())).create(bestConfiguration);

        System.out.println("Optimization complete. Total time: " + optimizationDuration + "ms. Best itinerary:");
        System.out.println(ItineraryFormatter.prettyPrint(bestItinerary));
    }
}
