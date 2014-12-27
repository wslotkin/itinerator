package itinerator.problem;

import cz.cvut.felk.cig.jcop.problem.*;
import cz.cvut.felk.cig.jcop.util.JcopRandom;
import itinerator.datamodel.Activity;
import itinerator.evaluator.ItineraryEvaluator;
import itinerator.evaluator.ItineraryFitness;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class ItineraryProblem extends BaseProblem implements GlobalSearchProblem, RandomConfigurationProblem {

    private final List<Activity> activities;
    private final DateTime startTime;
    private final int maxPosition;

    public ItineraryProblem(List<Activity> activities, DateTime startTime) {
        this.activities = activities;
        this.startTime = startTime;
        maxPosition = activities.size() + 1;
    }

    @Override
    public boolean isSolution(Configuration configuration) {
        return true;
    }

    @Override
    public Fitness getDefaultFitness() {
        return new ItineraryFitness(activities, new ItineraryEvaluator(), startTime);
    }

    @Override
    public OperationIterator getOperationIterator(Configuration configuration) {
        return null;
    }

    @Override
    public Integer getMaximum(int index) {
        return maxPosition;
    }

    @Override
    public Configuration getRandomConfiguration() {
        List<Integer> attributes = new ArrayList<>();
        for (Activity activity : activities) {
            boolean isIncluded = JcopRandom.nextBoolean();
            attributes.add(isIncluded ? JcopRandom.nextInt(maxPosition) + 1 : 0);
        }
        return new Configuration(attributes);
    }
}
