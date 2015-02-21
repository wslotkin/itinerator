package itinerator.evaluator;

import itinerator.datamodel.*;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static itinerator.TestConstants.DELTA;
import static itinerator.datamodel.ActivityType.ACTIVITY;
import static itinerator.datamodel.ActivityType.FOOD;
import static itinerator.evaluator.MealEvaluator.INCORRECT_MEAL_PENALTY;
import static itinerator.itinerary.TimeUtil.*;
import static org.junit.Assert.assertEquals;

public class MealEvaluatorTest {
    private static final DateTime T_0 = new DateTime(2015, 2, 6, START_OF_DINNER_WINDOW.getHourOfDay(), 0);
    private static final DateTime T_1 = new DateTime(2015, 2, 6, END_OF_DINNER_WINDOW.getHourOfDay(), 0);
    private static final DateTime T_2 = new DateTime(2015, 2, 7, START_OF_BREAKFAST_WINDOW.getHourOfDay(), 0);
    private static final DateTime T_3 = new DateTime(2015, 2, 7, END_OF_BREAKFAST_WINDOW.getHourOfDay(), 0);
    private static final DateTime T_4 = new DateTime(2015, 2, 7, START_OF_LUNCH_WINDOW.getHourOfDay(), 0);
    private static final DateTime T_5 = new DateTime(2015, 2, 7, END_OF_LUNCH_WINDOW.getHourOfDay(), 0);
    private static final DateTime T_6 = new DateTime(2015, 2, 7, START_OF_DINNER_WINDOW.getHourOfDay(), 0);
    private static final DateTime T_7 = new DateTime(2015, 2, 7, END_OF_DINNER_WINDOW.getHourOfDay(), 0);
    private static final DateTime T_8 = new DateTime(2015, 2, 8, START_OF_BREAKFAST_WINDOW.getHourOfDay(), 0);
    private static final DateTime T_9 = new DateTime(2015, 2, 8, END_OF_BREAKFAST_WINDOW.getHourOfDay(), 0);
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
    private MealEvaluator mealEvaluator;

    @Before
    public void before() {
        events = newArrayList(EVENT_1, EVENT_2, EVENT_3, EVENT_4, EVENT_5, EVENT_6, EVENT_7, EVENT_8, EVENT_9);

        mealEvaluator = new MealEvaluator();
    }

    @Test
    public void whenItineraryIsEmptyShouldReturnZero() {
        double result = mealEvaluator.evaluate(new Itinerary(new ArrayList<>()));

        assertEquals(0.0, result, DELTA);
    }

    @Test
    public void whenItineraryHasCorrectMealEventsShouldReturnZero() {
        double result = mealEvaluator.evaluate(new Itinerary(events));

        assertEquals(0.0, result, DELTA);
    }

    @Test
    public void whenItineraryIsMissingExpectedMealEventShouldReturnPenalty() {
        transformEventAtIndexToType(0, ACTIVITY);
        double result = mealEvaluator.evaluate(new Itinerary(events));

        assertEquals(INCORRECT_MEAL_PENALTY, result, DELTA);
    }

    @Test
    public void whenItineraryHasExtraMealEventShouldReturnPenalty() {
        transformEventAtIndexToType(1, FOOD);
        double result = mealEvaluator.evaluate(new Itinerary(events));

        assertEquals(INCORRECT_MEAL_PENALTY, result, DELTA);
    }

    private void transformEventAtIndexToType(int index, ActivityType type) {
        Event event = events.get(index);
        Event transformedEvent = new TestEventBuilder()
                .setActivity(new TestActivityBuilder()
                        .setType(type)
                        .build())
                .setEventTime(event.getEventTime())
                .build();
        events.remove(event);
        events.add(index, transformedEvent);
    }

    private static Event event(ActivityType type, DateTime startTime, DateTime endTime) {
        return new TestEventBuilder()
                .setActivity(new TestActivityBuilder()
                        .setType(type)
                        .build())
                .setEventTime(new Interval(startTime, endTime))
                .build();
    }
}