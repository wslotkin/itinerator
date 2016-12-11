package com.github.wslotkin.itinerator.generator.itinerary;

import com.github.wslotkin.itinerator.generator.calculators.RoundingTravelTimeCalculator;
import com.github.wslotkin.itinerator.generator.datamodel.Activity;
import com.github.wslotkin.itinerator.generator.datamodel.Event;
import com.github.wslotkin.itinerator.generator.datamodel.Itinerary;
import cz.cvut.felk.cig.jcop.problem.Configuration;

import java.time.LocalDateTime;
import java.util.List;

public class ItineraryFactory {

    private final List<Activity> activities;
    private final LocalDateTime startTime;
    private final RoundingTravelTimeCalculator travelTimeCalculator;
    private final LocalDateTime endTime;
    private final List<Event> fixedEvents;

    public ItineraryFactory(List<Activity> activities,
                            LocalDateTime startTime,
                            LocalDateTime endTime,
                            RoundingTravelTimeCalculator travelTimeCalculator,
                            List<Event> fixedEvents) {
        this.activities = activities;
        this.startTime = startTime;
        this.travelTimeCalculator = travelTimeCalculator;
        this.endTime = endTime;
        this.fixedEvents = fixedEvents;
    }

    public Itinerary create(Configuration configuration) {
        ItineraryBuilder builder = new ItineraryBuilder(startTime, endTime, travelTimeCalculator, fixedEvents);
        for (int i = 0; i < activities.size(); i++) {
            Integer position = configuration.valueAt(i);
            if (position >= 0) {
                Activity activity = activities.get(i);
                builder.addActivityAtPosition(activity, position);
            }
        }
        return builder.build();
    }
}
