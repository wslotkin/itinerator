package itinerator.evaluator;

import itinerator.datamodel.ActivityBuilder;
import itinerator.datamodel.Event;
import itinerator.datamodel.Itinerary;
import itinerator.datamodel.TestEventBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static com.google.common.collect.Lists.newArrayList;
import static itinerator.TestUtil.DELTA;
import static itinerator.evaluator.EventEvaluators.costEvaluator;
import static org.junit.Assert.assertEquals;

public class CostEvaluatorTest {
    private static final double COST_PENALTY = -10.0;
    private static final double FIRST_EVENT_COST = 1.2;
    private static final double SECOND_EVENT_COST = 2.3;
    private static final Event FIRST_EVENT = eventWithCost(FIRST_EVENT_COST);
    private static final Event SECOND_EVENT = eventWithCost(SECOND_EVENT_COST);

    private Evaluator<Itinerary> costEvaluator;

    @Before
    public void before() {
        costEvaluator = new EventEvaluators(costEvaluator(COST_PENALTY));
    }

    @Test
    public void whenItineraryIsEmptyShouldReturnZero() {
        double result = costEvaluator.applyAsDouble(new Itinerary(new ArrayList<>()));

        assertEquals(0.0, result, DELTA);
    }

    @Test
    public void whenItineraryHasOnlyOneEventReturnsCostOfEventScaledByMultiplier() {
        double result = costEvaluator.applyAsDouble(new Itinerary(newArrayList(FIRST_EVENT)));

        double expectedResult = FIRST_EVENT_COST * COST_PENALTY;

        assertEquals(expectedResult, result, DELTA);
    }

    @Test
    public void returnsSumOfCostsOfAllEventsScaledByMultiplier() {
        double result = costEvaluator.applyAsDouble(new Itinerary(newArrayList(FIRST_EVENT, SECOND_EVENT)));

        double expectedResult = (FIRST_EVENT_COST + SECOND_EVENT_COST) * COST_PENALTY;

        assertEquals(expectedResult, result, DELTA);
    }

    private static Event eventWithCost(double cost) {
        return new TestEventBuilder()
                .setActivity(new ActivityBuilder()
                        .setCost(cost)
                        .build())
                .build();
    }
}