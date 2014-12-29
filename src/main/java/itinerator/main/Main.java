package itinerator.main;

import cz.cvut.felk.cig.jcop.algorithm.geneticalgorithm.GeneticAlgorithm;
import cz.cvut.felk.cig.jcop.solver.SimpleSolver;
import cz.cvut.felk.cig.jcop.solver.condition.IterationCondition;
import itinerator.data.DataLoader;
import itinerator.datamodel.Activity;
import itinerator.problem.ItineraryProblem;
import org.joda.time.DateTime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class Main {

    public static void main(String[] args) throws IOException {
        Collection<Activity> activities = new DataLoader().loadData(Main.class.getClassLoader().getResource("beijingspots.csv").getPath());
        SimpleSolver solver = new SimpleSolver(new GeneticAlgorithm(10, 0.2), new ItineraryProblem(new ArrayList<>(activities), new DateTime()));
        solver.addStopCondition(new IterationCondition(10));
        solver.run();
    }
}
