package itinerator.solver;

import cz.cvut.felk.cig.jcop.problem.*;
import cz.cvut.felk.cig.jcop.util.JcopRandom;
import itinerator.config.EvaluationConfig;
import itinerator.datamodel.Activity;
import itinerator.datamodel.Event;
import itinerator.datamodel.Itinerary;
import itinerator.evaluator.Evaluator;
import itinerator.evaluator.ItineraryEvaluators;
import itinerator.evaluator.ItineraryFitness;
import itinerator.itinerary.ItineraryFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ItineraryProblem extends BaseProblem implements GlobalSearchProblem, RandomConfigurationProblem {

    private final List<Activity> activities;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final ItineraryFactory itineraryFactory;
    private final EvaluationConfig evaluationConfig;

    public ItineraryProblem(List<Activity> activities,
                            LocalDateTime startTime,
                            LocalDateTime endTime,
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
        LocalDateTime endTimeOfPrevious = startTime;
        for (Event event : itinerary.getEvents()) {
            LocalDateTime eventStart = event.getEventTime().getStart();
            LocalDateTime eventEnd = event.getEventTime().getEnd();
            if (endTimeOfPrevious.isAfter(eventStart) || startTime.isAfter(eventStart) || endTime.isBefore(eventEnd)) {
                return false;
            }
            endTimeOfPrevious = eventEnd;
        }
        return true;
    }

    @Override
    public Fitness getDefaultFitness() {
        Evaluator<Itinerary> evaluator = ItineraryEvaluators.createEvaluators(evaluationConfig);
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
