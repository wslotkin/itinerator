package itinerator.evaluator;

import itinerator.datamodel.Event;
import itinerator.datamodel.ImmutableEvent;
import itinerator.datamodel.ImmutableItinerary;
import itinerator.datamodel.Itinerary;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static com.google.common.collect.Lists.newArrayList;
import static itinerator.TestUtil.DELTA;
import static itinerator.evaluator.EventEvaluators.movementEvaluator;
import static org.junit.Assert.assertEquals;

public class MovementEvaluatorTest {

    private static final double AREA_HOPPING_PENALTY = -50.0;
    private static final double AREA_HOPPING_THRESHOLD = 15.0;
    private static final double TRAVEL_TIME_BELOW_THRESHOLD = AREA_HOPPING_THRESHOLD - DELTA;
    private static final double TRAVEL_TIME_ABOVE_THRESHOLD = AREA_HOPPING_THRESHOLD + DELTA;
    private static final Event FIRST_EVENT = ImmutableEvent.builder().travelTime(TRAVEL_TIME_ABOVE_THRESHOLD).build();
    private static final Event SECOND_EVENT = ImmutableEvent.builder().travelTime(TRAVEL_TIME_BELOW_THRESHOLD).build();
    private static final Event THIRD_EVENT = ImmutableEvent.builder().travelTime(TRAVEL_TIME_ABOVE_THRESHOLD).build();

    private Evaluator<Itinerary> movementEvaluator;

    @Before
    public void before() {
        movementEvaluator = new EventEvaluators(movementEvaluator(AREA_HOPPING_PENALTY, AREA_HOPPING_THRESHOLD));
    }

    @Test
    public void whenItineraryIsEmptyShouldReturnZero() {
        double result = movementEvaluator.applyAsDouble(ImmutableItinerary.of(new ArrayList<>()));

        assertEquals(0.0, result, DELTA);
    }

    @Test
    public void returnsNumberOfEventsWithTravelTimeGreaterThanThresholdTimesPenalty() {
        double result = movementEvaluator.applyAsDouble(ImmutableItinerary.of(newArrayList(FIRST_EVENT, SECOND_EVENT, THIRD_EVENT)));

        double expectedResult = 2.0 * AREA_HOPPING_PENALTY;

        assertEquals(expectedResult, result, DELTA);
    }
}