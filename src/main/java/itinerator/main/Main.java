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
import itinerator.problem.ItineraryProblem;
import org.joda.time.DateTime;

import java.io.IOException;
import java.util.List;

import static com.google.common.collect.Iterables.getOnlyElement;

public class Main {

    public static void main(String[] args) throws IOException {
        String dataFile = Main.class.getClassLoader().getResource("beijingspots.csv").getPath();
        List<Activity> activities = new DataLoader().loadData(dataFile);
        DateTime startTime = new DateTime().minusDays(3);
        DateTime endTime = new DateTime();

        SimpleSolver solver = new SimpleSolver(new GeneticAlgorithm(100, 0.2), new ItineraryProblem(activities, startTime, endTime));
        solver.addStopCondition(new IterationCondition(100));
        solver.run();

        Configuration bestConfiguration = getOnlyElement(solver.getResult().getResultEntries()).getBestConfiguration();
        Itinerary bestItinerary = new ItineraryFactory(activities, startTime, endTime, new TravelTimeCalculator(new DistanceCalculator())).create(bestConfiguration);
        System.out.println(bestItinerary);
    }
}
