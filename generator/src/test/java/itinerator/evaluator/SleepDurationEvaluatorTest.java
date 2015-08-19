package itinerator.evaluator;

import itinerator.datamodel.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static com.google.common.collect.Lists.newArrayList;
import static itinerator.TestConstants.DELTA;
import static itinerator.datamodel.ActivityType.ACTIVITY;
import static itinerator.datamodel.ActivityType.SLEEP;
import static itinerator.itinerary.TimeUtil.TARGET_MINUTES_OF_SLEEP;
import static org.junit.Assert.assertEquals;

public class SleepDurationEvaluatorTest {

    private static final double MISSING_SLEEP_MINUTES_PENALTY = -2.0;

    private SleepDurationEvaluator sleepDurationEvaluator;

    @Before
    public void before() {
        sleepDurationEvaluator = new SleepDurationEvaluator(MISSING_SLEEP_MINUTES_PENALTY);
    }

    @Test
    public void whenItineraryIsEmptyShouldReturnZero() {
        double result = sleepDurationEvaluator.evaluate(new Itinerary(new ArrayList<>()));

        assertEquals(0.0, result, DELTA);
    }

    @Test
    public void whenAllSleepEventsAreTargetDurationOrLongerShouldReturnZero() {
        double result = sleepDurationEvaluator.evaluate(new Itinerary(newArrayList(createEvent(SLEEP, TARGET_MINUTES_OF_SLEEP),
                createEvent(ACTIVITY, 60),
                createEvent(SLEEP, TARGET_MINUTES_OF_SLEEP + 60))));

        assertEquals(0.0, result, DELTA);
    }

    @Test
    public void whenSleepEventsAreShorterThanTargetDurationShouldReturnSumOfSleepDeficitMultipliedByPenalty() {
        int deficitOnFirstEvent = 10;
        int deficitOnSecondEvent = 20;

        double result = sleepDurationEvaluator.evaluate(new Itinerary(newArrayList(createEvent(SLEEP, TARGET_MINUTES_OF_SLEEP - deficitOnFirstEvent),
                createEvent(ACTIVITY, 60),
                createEvent(SLEEP, TARGET_MINUTES_OF_SLEEP - deficitOnSecondEvent))));

        double expectedResult = MISSING_SLEEP_MINUTES_PENALTY * (deficitOnFirstEvent + deficitOnSecondEvent);

        assertEquals(expectedResult, result, DELTA);
    }

    @Test
    public void aSleepEventLongerThanTargetDurationDoesNotAffectTotalDeficit() {
        int increment = 10;

        double result = sleepDurationEvaluator.evaluate(new Itinerary(newArrayList(createEvent(SLEEP, TARGET_MINUTES_OF_SLEEP - increment),
                createEvent(ACTIVITY, 60),
                createEvent(SLEEP, TARGET_MINUTES_OF_SLEEP + increment))));

        double expectedResult = MISSING_SLEEP_MINUTES_PENALTY * increment;

        assertEquals(expectedResult, result, DELTA);
    }

    private static Event createEvent(ActivityType type, int duration) {
        return new TestEventBuilder()
                .setActivity(new ActivityBuilder()
                        .setType(type)
                        .setDuration(duration)
                        .build())
                .build();
    }
}