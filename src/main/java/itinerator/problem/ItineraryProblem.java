package itinerator.problem;

import cz.cvut.felk.cig.jcop.problem.*;
import cz.cvut.felk.cig.jcop.util.JcopRandom;
import itinerator.calculators.DistanceCalculator;
import itinerator.calculators.TravelTimeCalculator;
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

    public ItineraryProblem(List<Activity> activities, DateTime startTime, DateTime endTime) {
        this.activities = activities;
        this.startTime = startTime;
        this.endTime = endTime;
        TravelTimeCalculator travelCalculator = new TravelTimeCalculator(new DistanceCalculator());
        itineraryFactory = new ItineraryFactory(activities, startTime, endTime, travelCalculator);
    }

    @Override
    public boolean isSolution(Configuration configuration) {
        Itinerary itinerary = itineraryFactory.create(configuration);
        DateTime endTimeOfPrevious = startTime;
        for (Event event : itinerary.getEvents()) {
            DateTime eventStart = event.getEventTime().getStart();
            DateTime eventEnd = event.getEventTime().getEnd();
            if (endTimeOfPrevious.isAfter(eventStart) || eventStart.isBefore(startTime) || eventEnd.isAfter(endTime)) {
                return false;
            }
            endTimeOfPrevious = eventEnd;
        }
        return true;
    }

    @Override
    public Fitness getDefaultFitness() {
        ItineraryEvaluator evaluator = new ItineraryEvaluator(new FunEvaluator(), new TravelEvaluator());
        return new ItineraryFitness(activities, evaluator, itineraryFactory);
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
