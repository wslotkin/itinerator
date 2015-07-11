package itinerator.evaluator;

import itinerator.datamodel.Event;
import itinerator.datamodel.Itinerary;
import itinerator.datamodel.TestEventBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static com.google.common.collect.Lists.newArrayList;
import static itinerator.TestConstants.DELTA;
import static org.junit.Assert.assertEquals;

public class MovementEvaluatorTest {

    private static final double AREA_HOPPING_PENALTY = -50.0;
    private static final double AREA_HOPPING_THRESHOLD = 15.0;
    private static final double TRAVEL_TIME_BELOW_THRESHOLD = AREA_HOPPING_THRESHOLD - DELTA;
    private static final double TRAVEL_TIME_ABOVE_THRESHOLD = AREA_HOPPING_THRESHOLD + DELTA;
    private static final Event FIRST_EVENT = new TestEventBuilder().setTravelTime(TRAVEL_TIME_ABOVE_THRESHOLD).build();
    private static final Event SECOND_EVENT = new TestEventBuilder().setTravelTime(TRAVEL_TIME_BELOW_THRESHOLD).build();
    private static final Event THIRD_EVENT = new TestEventBuilder().setTravelTime(TRAVEL_TIME_ABOVE_THRESHOLD).build();

    private MovementEvaluator movementEvaluator;

    @Before
    public void before() {
        movementEvaluator = new MovementEvaluator(AREA_HOPPING_PENALTY, AREA_HOPPING_THRESHOLD);
    }

    @Test
    public void whenItineraryIsEmptyShouldReturnZero() {
        double result = movementEvaluator.evaluate(new Itinerary(new ArrayList<>()));

        assertEquals(0.0, result, DELTA);
    }

    @Test
    public void returnsNumberOfEventsWithTravelTimeGreaterThanThresholdTimesPenalty() {
        double result = movementEvaluator.evaluate(new Itinerary(newArrayList(FIRST_EVENT, SECOND_EVENT, THIRD_EVENT)));

        double expectedResult = 2.0 * AREA_HOPPING_PENALTY;

        assertEquals(expectedResult, result, DELTA);
    }
}