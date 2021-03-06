package com.github.wslotkin.itinerator.generator.evaluator;

import com.github.wslotkin.itinerator.generator.datamodel.*;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static com.github.wslotkin.itinerator.generator.TestUtil.DELTA;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;

public class RequiredEventsEvaluatorTest {
    private static final Activity FIRST_ACTIVITY = activity("1");
    private static final Activity SECOND_ACTIVITY = activity("2");
    private static final Activity THIRD_ACTIVITY = activity("3");
    private static final Set<Activity> TARGET_ACTIVITIES = newHashSet(SECOND_ACTIVITY, THIRD_ACTIVITY);
    private static final double PER_EVENT_PENALTY = -10.0;

    private Evaluator<Itinerary> requiredEventsEvaluator;

    @Before
    public void before() {
        requiredEventsEvaluator = new RequiredEventsEvaluator(TARGET_ACTIVITIES, PER_EVENT_PENALTY);
    }

    @Test
    public void whenItineraryIsEmptyShouldReturnPenaltyTimesNumberOfRequiredEvents() {
        double result = requiredEventsEvaluator.applyAsDouble(toItinerary(newArrayList()));

        double expectedResult = 2.0 * PER_EVENT_PENALTY;

        assertEquals(expectedResult, result, DELTA);
    }

    @Test
    public void whenItineraryHasNoRequiredEventsShouldReturnPenaltyTimesNumberOfRequiredEvents() {
        double result = requiredEventsEvaluator.applyAsDouble(toItinerary(newArrayList(FIRST_ACTIVITY)));

        double expectedResult = 2.0 * PER_EVENT_PENALTY;

        assertEquals(expectedResult, result, DELTA);
    }

    @Test
    public void whenItineraryHasAllButOneRequiredEventsShouldReturnPenalty() {
        double result = requiredEventsEvaluator.applyAsDouble(toItinerary(newArrayList(FIRST_ACTIVITY, SECOND_ACTIVITY)));

        assertEquals(PER_EVENT_PENALTY, result, DELTA);
    }

    @Test
    public void whenItineraryHasAllRequiredEventsShouldReturnZero() {
        double result = requiredEventsEvaluator.applyAsDouble(toItinerary(newArrayList(FIRST_ACTIVITY, SECOND_ACTIVITY, THIRD_ACTIVITY)));

        assertEquals(0.0, result, DELTA);
    }

    private static Itinerary toItinerary(List<Activity> activities) {
        List<Event> events = activities.stream()
                .map(activity -> ImmutableEvent.builder().activity(activity).build())
                .collect(toList());
        return ImmutableItinerary.of(events);
    }

    private static Activity activity(String name) {
        return ImmutableActivity.builder().id(name).build();
    }
}