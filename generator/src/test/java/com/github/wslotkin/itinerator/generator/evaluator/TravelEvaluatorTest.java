package com.github.wslotkin.itinerator.generator.evaluator;

import com.github.wslotkin.itinerator.generator.datamodel.Event;
import com.github.wslotkin.itinerator.generator.datamodel.ImmutableEvent;
import com.github.wslotkin.itinerator.generator.datamodel.ImmutableItinerary;
import com.github.wslotkin.itinerator.generator.datamodel.Itinerary;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static com.github.wslotkin.itinerator.generator.evaluator.EventEvaluators.travelEvaluator;
import static com.github.wslotkin.itinerator.generator.performance.TestUtil.DELTA;
import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;

public class TravelEvaluatorTest {

    private static final double FIRST_EVENT_TRAVEL_TIME = 1.2;
    private static final double SECOND_EVENT_TRAVEL_TIME = 2.3;
    private static final double TRAVEL_TIME_PENALTY = -20.0;
    private static final Event FIRST_EVENT = ImmutableEvent.builder().travelTime(FIRST_EVENT_TRAVEL_TIME).build();
    private static final Event SECOND_EVENT = ImmutableEvent.builder().travelTime(SECOND_EVENT_TRAVEL_TIME).build();

    private Evaluator<Itinerary> travelEvaluator;

    @Before
    public void before() {
        travelEvaluator = new EventEvaluators(travelEvaluator(TRAVEL_TIME_PENALTY));
    }

    @Test
    public void whenItineraryIsEmptyShouldReturnZero() {
        double result = travelEvaluator.applyAsDouble(ImmutableItinerary.of(new ArrayList<>()));

        assertEquals(0.0, result, DELTA);
    }

    @Test
    public void whenItineraryHasOnlyOneEventReturnsTravelTimeOfEventScaledByMultiplier() {
        double result = travelEvaluator.applyAsDouble(ImmutableItinerary.of(newArrayList(FIRST_EVENT)));

        double expectedResult = FIRST_EVENT_TRAVEL_TIME * TRAVEL_TIME_PENALTY;

        assertEquals(expectedResult, result, DELTA);
    }

    @Test
    public void returnsSumOfTravelTimesOfAllEventsScaledByMultiplier() {
        double result = travelEvaluator.applyAsDouble(ImmutableItinerary.of(newArrayList(FIRST_EVENT, SECOND_EVENT)));

        double expectedResult = (FIRST_EVENT_TRAVEL_TIME + SECOND_EVENT_TRAVEL_TIME) * TRAVEL_TIME_PENALTY;

        assertEquals(expectedResult, result, DELTA);
    }
}