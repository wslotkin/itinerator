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

    public ItineraryProblem(List<Activity> activities, DateTime startTime) {
        this.activities = activities;
        this.startTime = startTime;
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
        return activities.size();
    }

    @Override
    public Configuration getRandomConfiguration() {
        List<Integer> attributes = new ArrayList<>();
        for (int i = 0; i < activities.size(); i++) {
            boolean isIncluded = JcopRandom.nextBoolean();
            attributes.add(isIncluded ? JcopRandom.nextInt(activities.size()) : -1);
        }
        return new Configuration(attributes);
    }
}
