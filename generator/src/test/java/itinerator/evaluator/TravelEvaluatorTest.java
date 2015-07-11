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

public class TravelEvaluatorTest {

    private static final double FIRST_EVENT_TRAVEL_TIME = 1.2;
    private static final double SECOND_EVENT_TRAVEL_TIME = 2.3;
    private static final double TRAVEL_TIME_PENALTY = -20.0;
    private static final Event FIRST_EVENT = new TestEventBuilder().setTravelTime(FIRST_EVENT_TRAVEL_TIME).build();
    private static final Event SECOND_EVENT = new TestEventBuilder().setTravelTime(SECOND_EVENT_TRAVEL_TIME).build();

    private TravelEvaluator travelEvaluator;

    @Before
    public void before() {
        travelEvaluator = new TravelEvaluator(TRAVEL_TIME_PENALTY);
    }

    @Test
    public void whenItineraryIsEmptyShouldReturnZero() {
        double result = travelEvaluator.evaluate(new Itinerary(new ArrayList<>()));

        assertEquals(0.0, result, DELTA);
    }

    @Test
    public void whenItineraryHasOnlyOneEventReturnsTravelTimeOfEventScaledByMultiplier() {
        double result = travelEvaluator.evaluate(new Itinerary(newArrayList(FIRST_EVENT)));

        double expectedResult = FIRST_EVENT_TRAVEL_TIME * TRAVEL_TIME_PENALTY;

        assertEquals(expectedResult, result, DELTA);
    }

    @Test
    public void returnsSumOfTravelTimesOfAllEventsScaledByMultiplier() {
        double result = travelEvaluator.evaluate(new Itinerary(newArrayList(FIRST_EVENT, SECOND_EVENT)));

        double expectedResult = (FIRST_EVENT_TRAVEL_TIME + SECOND_EVENT_TRAVEL_TIME) * TRAVEL_TIME_PENALTY;

        assertEquals(expectedResult, result, DELTA);
    }
}