package itinerator.solver;

import cz.cvut.felk.cig.jcop.problem.*;
import cz.cvut.felk.cig.jcop.util.JcopRandom;
import itinerator.config.EvaluationConfig;
import itinerator.datamodel.Activity;
import itinerator.datamodel.Event;
import itinerator.datamodel.Itinerary;
import itinerator.evaluator.*;
import itinerator.itinerary.ItineraryFactory;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class ItineraryProblem extends BaseProblem implements GlobalSearchProblem, RandomConfigurationProblem {

    private final List<Activity> activities;
    private final DateTime startTime;
    private final DateTime endTime;
    private final ItineraryFactory itineraryFactory;
    private final EvaluationConfig evaluationConfig;

    public ItineraryProblem(List<Activity> activities,
                            DateTime startTime,
                            DateTime endTime,
                            ItineraryFactory itineraryFactory,
                            EvaluationConfig evaluationConfig) {
        this.activities = activities;
        this.startTime = startTime;
        this.endTime = endTime;
        this.itineraryFactory = itineraryFactory;
        this.evaluationConfig = evaluationConfig;
    }

    @Override
    public boolean isSolution(Configuration configuration) {
        Itinerary itinerary = itineraryFactory.create(configuration);
        long endTimeOfPrevious = startTime.getMillis();
        for (Event event : itinerary.getEvents()) {
            long eventStart = event.getEventTime().getStartMillis();
            long eventEnd = event.getEventTime().getEndMillis();
            if (endTimeOfPrevious > eventStart || startTime.isAfter(eventStart) || endTime.isBefore(eventEnd)) {
                return false;
            }
            endTimeOfPrevious = eventEnd;
        }
        return true;
    }

    @Override
    public Fitness getDefaultFitness() {
        CompositeEvaluator evaluator = new CompositeEvaluator(new FunEvaluator(),
                new TravelEvaluator(evaluationConfig.getTravelTimePenalty()),
                new MovementEvaluator(evaluationConfig.getAreaHoppingPenalty(), evaluationConfig.getAreaHoppingThreshold()),
                new CostEvaluator(evaluationConfig.getCostPenalty()),
                new SleepEvaluator(evaluationConfig.getIncorrectSleepPenalty()),
                new MealEvaluator(evaluationConfig.getIncorrectMealPenalty()),
                new HoursEvaluator(evaluationConfig.getInvalidHoursPenalty()));
        return new ItineraryFitness(evaluator, itineraryFactory);
    }

    @Override
    public OperationIterator getOperationIterator(Configuration configuration) {
        return null;
    }

    @Override
    public Integer getMaximum(int index) {
        return Integer.MAX_VALUE;
    }

    @Override
    public Configuration getRandomConfiguration() {
        List<Integer> attributes = new ArrayList<>();
        for (int i = 0; i < activities.size(); i++) {
            boolean isIncluded = JcopRandom.nextBoolean();
            attributes.add(isIncluded ? JcopRandom.nextInt(Integer.MAX_VALUE) : -1);
        }
        return new Configuration(attributes);
    }
}
