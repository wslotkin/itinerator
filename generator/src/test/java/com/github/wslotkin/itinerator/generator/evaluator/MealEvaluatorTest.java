package com.github.wslotkin.itinerator.generator.evaluator;

import com.github.wslotkin.itinerator.generator.datamodel.*;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.github.wslotkin.itinerator.generator.datamodel.ActivityType.ACTIVITY;
import static com.github.wslotkin.itinerator.generator.datamodel.ActivityType.FOOD;
import static com.github.wslotkin.itinerator.generator.itinerary.TimeUtil.*;
import static com.github.wslotkin.itinerator.generator.TestUtil.DELTA;
import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;

public class MealEvaluatorTest {
    private static final double INCORRECT_MEAL_PENALTY = -20.0;
    private static final LocalDateTime T_0 = LocalDateTime.of(2015, 2, 6, DINNER_WINDOW.getStart().getHour(), 0);
    private static final LocalDateTime T_1 = LocalDateTime.of(2015, 2, 6, DINNER_WINDOW.getEnd().getHour(), 0);
    private static final LocalDateTime T_2 = LocalDateTime.of(2015, 2, 7, BREAKFAST_WINDOW.getStart().getHour(), 0);
    private static final LocalDateTime T_3 = LocalDateTime.of(2015, 2, 7, BREAKFAST_WINDOW.getEnd().getHour(), 0);
    private static final LocalDateTime T_4 = LocalDateTime.of(2015, 2, 7, LUNCH_WINDOW.getStart().getHour(), 0);
    private static final LocalDateTime T_5 = LocalDateTime.of(2015, 2, 7, LUNCH_WINDOW.getEnd().getHour(), 0);
    private static final LocalDateTime T_6 = LocalDateTime.of(2015, 2, 7, DINNER_WINDOW.getStart().getHour(), 0);
    private static final LocalDateTime T_7 = LocalDateTime.of(2015, 2, 7, DINNER_WINDOW.getEnd().getHour(), 0);
    private static final LocalDateTime T_8 = LocalDateTime.of(2015, 2, 8, BREAKFAST_WINDOW.getStart().getHour(), 0);
    private static final LocalDateTime T_9 = LocalDateTime.of(2015, 2, 8, BREAKFAST_WINDOW.getEnd().getHour(), 0);
    private static final Event EVENT_1 = event(FOOD, T_0, T_1);
    private static final Event EVENT_2 = event(ACTIVITY, T_1, T_2);
    private static final Event EVENT_3 = event(FOOD, T_2, T_3);
    private static final Event EVENT_4 = event(ACTIVITY, T_3, T_4);
    private static final Event EVENT_5 = event(FOOD, T_4, T_5);
    private static final Event EVENT_6 = event(ACTIVITY, T_5, T_6);
    private static final Event EVENT_7 = event(FOOD, T_6, T_7);
    private static final Event EVENT_8 = event(ACTIVITY, T_7, T_8);
    private static final Event EVENT_9 = event(FOOD, T_8, T_9);

    private List<Event> events;
    private Evaluator<Itinerary> mealEvaluator;

    @Before
    public void before() {
        events = newArrayList(EVENT_1, EVENT_2, EVENT_3, EVENT_4, EVENT_5, EVENT_6, EVENT_7, EVENT_8, EVENT_9);

        mealEvaluator = new SubitineraryEvaluators(new DaySubitineraryProvider(), new MealEvaluator(INCORRECT_MEAL_PENALTY));
    }

    @Test
    public void whenItineraryIsEmptyShouldReturnZero() {
        double result = mealEvaluator.applyAsDouble(ImmutableItinerary.of(new ArrayList<>()));

        assertEquals(0.0, result, DELTA);
    }

    @Test
    public void whenItineraryHasCorrectMealEventsShouldReturnZero() {
        double result = mealEvaluator.applyAsDouble(ImmutableItinerary.of(events));

        assertEquals(0.0, result, DELTA);
    }

    @Test
    public void whenItineraryIsMissingExpectedMealEventShouldReturnPenalty() {
        transformEventAtIndexToType(0, ACTIVITY);
        double result = mealEvaluator.applyAsDouble(ImmutableItinerary.of(events));

        assertEquals(INCORRECT_MEAL_PENALTY, result, DELTA);
    }

    @Test
    public void whenItineraryHasExtraMealEventShouldReturnPenalty() {
        transformEventAtIndexToType(1, FOOD);
        double result = mealEvaluator.applyAsDouble(ImmutableItinerary.of(events));

        assertEquals(INCORRECT_MEAL_PENALTY, result, DELTA);
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