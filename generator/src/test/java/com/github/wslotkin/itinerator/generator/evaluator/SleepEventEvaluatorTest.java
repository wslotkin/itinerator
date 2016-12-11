package com.github.wslotkin.itinerator.generator.evaluator;

import com.github.wslotkin.itinerator.generator.datamodel.*;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.github.wslotkin.itinerator.generator.datamodel.ActivityType.ACTIVITY;
import static com.github.wslotkin.itinerator.generator.datamodel.ActivityType.SLEEP;
import static com.github.wslotkin.itinerator.generator.performance.TestUtil.DELTA;
import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;

public class SleepEventEvaluatorTest {
    private static final double INCORRECT_SLEEP_PENALTY = -100.0;
    private static final LocalDateTime T_0 = LocalDateTime.of(2015, 2, 6, 12, 0);
    private static final LocalDateTime T_1 = LocalDateTime.of(2015, 2, 6, 22, 0);
    private static final LocalDateTime T_2 = LocalDateTime.of(2015, 2, 7, 9, 0);
    private static final LocalDateTime T_3 = LocalDateTime.of(2015, 2, 7, 22, 0);
    private static final LocalDateTime T_4 = LocalDateTime.of(2015, 2, 8, 9, 0);
    private static final LocalDateTime T_5 = LocalDateTime.of(2015, 2, 8, 12, 0);
    private static final Event EVENT_1 = event(ACTIVITY, T_0, T_1);
    private static final Event EVENT_2 = event(SLEEP, T_1, T_2);
    private static final Event EVENT_3 = event(ACTIVITY, T_2, T_3);
    private static final Event EVENT_4 = event(SLEEP, T_3, T_4);
    private static final Event EVENT_5 = event(ACTIVITY, T_4, T_5);

    private List<Event> events;
    private Evaluator<Itinerary> sleepEventEvaluator;

    @Before
    public void before() {
        events = newArrayList(EVENT_1, EVENT_2, EVENT_3, EVENT_4, EVENT_5);

        sleepEventEvaluator = new SubitineraryEvaluators(new DaySubitineraryProvider(), new SleepEventEvaluator(INCORRECT_SLEEP_PENALTY));
    }

    @Test
    public void whenItineraryIsEmptyShouldReturnZero() {
        double result = sleepEventEvaluator.applyAsDouble(ImmutableItinerary.of(new ArrayList<>()));

        assertEquals(0.0, result, DELTA);
    }

    @Test
    public void whenItineraryHasCorrectSleepEventsShouldReturnZero() {
        double result = sleepEventEvaluator.applyAsDouble(ImmutableItinerary.of(events));

        assertEquals(0.0, result, DELTA);
    }

    @Test
    public void whenItineraryIsMissingExpectedSleepEventShouldReturnPenalty() {
        transformEventAtIndexToType(1, ACTIVITY);
        double result = sleepEventEvaluator.applyAsDouble(ImmutableItinerary.of(events));

        assertEquals(INCORRECT_SLEEP_PENALTY, result, DELTA);
    }

    @Test
    public void whenItineraryHasExtraSleepEventShouldReturnPenalty() {
        transformEventAtIndexToType(2, SLEEP);
        double result = sleepEventEvaluator.applyAsDouble(ImmutableItinerary.of(events));

        assertEquals(INCORRECT_SLEEP_PENALTY, result, DELTA);
    }

    private void transformEventAtIndexToType(int index, ActivityType type) {
        Event event = events.get(index);
        Event transformedEvent = ImmutableEvent.builder()
                .activity(ImmutableActivity.builder()
                        .type(type)
                        .build())
                .eventTime(event.getEventTime())
                .build();
        events.remove(event);
        events.add(index, transformedEvent);
    }

    private static Event event(ActivityType type, LocalDateTime startTime, LocalDateTime endTime) {
        return ImmutableEvent.builder()
                .activity(ImmutableActivity.builder()
                        .type(type)
                        .build())
                .eventTime(Range.of(startTime, endTime))
                .build();
    }
}