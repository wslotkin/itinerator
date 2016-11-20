package itinerator.evaluator;

import itinerator.datamodel.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static com.google.common.collect.Lists.newArrayList;
import static itinerator.TestUtil.DELTA;
import static itinerator.datamodel.ActivityType.ACTIVITY;
import static itinerator.datamodel.ActivityType.SLEEP;
import static itinerator.evaluator.EventEvaluators.sleepEvaluator;
import static itinerator.itinerary.TimeUtil.TARGET_MINUTES_OF_SLEEP;
import static org.junit.Assert.assertEquals;

public class SleepDurationEvaluatorTest {

    private static final double MISSING_SLEEP_MINUTES_PENALTY = -2.0;

    private Evaluator<Itinerary> sleepDurationEvaluator;

    @Before
    public void before() {
        sleepDurationEvaluator = new EventEvaluators(sleepEvaluator(MISSING_SLEEP_MINUTES_PENALTY));
    }

    @Test
    public void whenItineraryIsEmptyShouldReturnZero() {
        double result = sleepDurationEvaluator.applyAsDouble(ImmutableItinerary.of(new ArrayList<>()));

        assertEquals(0.0, result, DELTA);
    }

    @Test
    public void whenAllSleepEventsAreTargetDurationOrLongerShouldReturnZero() {
        double result = sleepDurationEvaluator.applyAsDouble(ImmutableItinerary.of(newArrayList(createEvent(SLEEP, TARGET_MINUTES_OF_SLEEP),
                createEvent(ACTIVITY, 60),
                createEvent(SLEEP, TARGET_MINUTES_OF_SLEEP + 60))));

        assertEquals(0.0, result, DELTA);
    }

    @Test
    public void whenSleepEventsAreShorterThanTargetDurationShouldReturnSumOfSleepDeficitMultipliedByPenalty() {
        int deficitOnFirstEvent = 10;
        int deficitOnSecondEvent = 20;

        double result = sleepDurationEvaluator.applyAsDouble(ImmutableItinerary.of(newArrayList(createEvent(SLEEP, TARGET_MINUTES_OF_SLEEP - deficitOnFirstEvent),
                createEvent(ACTIVITY, 60),
                createEvent(SLEEP, TARGET_MINUTES_OF_SLEEP - deficitOnSecondEvent))));

        double expectedResult = MISSING_SLEEP_MINUTES_PENALTY * (deficitOnFirstEvent + deficitOnSecondEvent);

        assertEquals(expectedResult, result, DELTA);
    }

    @Test
    public void aSleepEventLongerThanTargetDurationDoesNotAffectTotalDeficit() {
        int increment = 10;

        double result = sleepDurationEvaluator.applyAsDouble(ImmutableItinerary.of(newArrayList(createEvent(SLEEP, TARGET_MINUTES_OF_SLEEP - increment),
                createEvent(ACTIVITY, 60),
                createEvent(SLEEP, TARGET_MINUTES_OF_SLEEP + increment))));

        double expectedResult = MISSING_SLEEP_MINUTES_PENALTY * increment;

        assertEquals(expectedResult, result, DELTA);
    }

    private static Event createEvent(ActivityType type, int duration) {
        return ImmutableEvent.builder()
                .activity(ImmutableActivity.builder()
                        .type(type)
                        .duration(duration)
                        .build())
                .build();
    }
}