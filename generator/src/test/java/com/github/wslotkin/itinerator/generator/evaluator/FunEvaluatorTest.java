package com.github.wslotkin.itinerator.generator.evaluator;

import com.github.wslotkin.itinerator.generator.datamodel.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static com.github.wslotkin.itinerator.generator.evaluator.EventEvaluators.FUN_NORMALIZATION_FACTOR;
import static com.github.wslotkin.itinerator.generator.evaluator.EventEvaluators.funEvaluator;
import static com.github.wslotkin.itinerator.generator.TestUtil.DELTA;
import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;

public class FunEvaluatorTest {

    private static final long FIRST_ACTIVITY_DURATION = 10L;
    private static final long SECOND_ACTIVITY_DURATION = 20L;
    private static final double FIRST_ACTIVITY_SCORE = 1.2;
    private static final double SECOND_ACTIVITY_SCORE = 2.3;
    private static final Event FIRST_EVENT = createEvent(FIRST_ACTIVITY_DURATION, FIRST_ACTIVITY_SCORE);
    private static final Event SECOND_EVENT = createEvent(SECOND_ACTIVITY_DURATION, SECOND_ACTIVITY_SCORE);

    private Evaluator<Itinerary> funEvaluator;

    @Before
    public void before() {
        funEvaluator = new EventEvaluators(funEvaluator());
    }

    @Test
    public void whenItineraryIsEmptyShouldReturnZero() {
        double result = funEvaluator.applyAsDouble(ImmutableItinerary.of(new ArrayList<>()));

        assertEquals(0.0, result, DELTA);
    }

    @Test
    public void whenItineraryHasOnlyOneEventReturnsScoreSquaredTimesDurationOfEvent() {
        double result = funEvaluator.applyAsDouble(ImmutableItinerary.of(newArrayList(FIRST_EVENT)));

        double expectedResult = FIRST_ACTIVITY_DURATION * FIRST_ACTIVITY_SCORE * FIRST_ACTIVITY_SCORE / FUN_NORMALIZATION_FACTOR;

        assertEquals(expectedResult, result, DELTA);
    }

    @Test
    public void returnsSumOfScoreSquaredTimesDurationOfAllActivities() {
        double result = funEvaluator.applyAsDouble(ImmutableItinerary.of(newArrayList(FIRST_EVENT, SECOND_EVENT)));

        double expectedResult = FIRST_ACTIVITY_DURATION * FIRST_ACTIVITY_SCORE * FIRST_ACTIVITY_SCORE / FUN_NORMALIZATION_FACTOR
                + SECOND_ACTIVITY_DURATION * SECOND_ACTIVITY_SCORE * SECOND_ACTIVITY_SCORE / FUN_NORMALIZATION_FACTOR;

        assertEquals(expectedResult, result, DELTA);
    }

    private static Event createEvent(long activityDuration, double activityScore) {
        return ImmutableEvent.builder()
                .activity(ImmutableActivity.builder()
                        .duration(activityDuration)
                        .score(activityScore)
                        .build())
                .build();
    }
}