package itinerator.evaluator;

import itinerator.datamodel.*;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static itinerator.TestUtil.DELTA;
import static itinerator.datamodel.ActivityType.ACTIVITY;
import static itinerator.datamodel.ActivityType.SLEEP;
import static org.junit.Assert.assertEquals;

public class SleepEventEvaluatorTest {
    private static final double INCORRECT_SLEEP_PENALTY = -100.0;
    private static final DateTime T_0 = new DateTime(2015, 2, 6, 12, 0);
    private static final DateTime T_1 = new DateTime(2015, 2, 6, 22, 0);
    private static final DateTime T_2 = new DateTime(2015, 2, 7, 9, 0);
    private static final DateTime T_3 = new DateTime(2015, 2, 7, 22, 0);
    private static final DateTime T_4 = new DateTime(2015, 2, 8, 9, 0);
    private static final DateTime T_5 = new DateTime(2015, 2, 8, 12, 0);
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

        sleepEventEvaluator = new ItineraryEvaluators(new DaySubitineraryProvider(), new SleepEventEvaluator(INCORRECT_SLEEP_PENALTY));
    }

    @Test
    public void whenItineraryIsEmptyShouldReturnZero() {
        double result = sleepEventEvaluator.applyAsDouble(new Itinerary(new ArrayList<>()));

        assertEquals(0.0, result, DELTA);
    }

    @Test
    public void whenItineraryHasCorrectSleepEventsShouldReturnZero() {
        double result = sleepEventEvaluator.applyAsDouble(new Itinerary(events));

        assertEquals(0.0, result, DELTA);
    }

    @Test
    public void whenItineraryIsMissingExpectedSleepEventShouldReturnPenalty() {
        transformEventAtIndexToType(1, ACTIVITY);
        double result = sleepEventEvaluator.applyAsDouble(new Itinerary(events));

        assertEquals(INCORRECT_SLEEP_PENALTY, result, DELTA);
    }

    @Test
    public void whenItineraryHasExtraSleepEventShouldReturnPenalty() {
        transformEventAtIndexToType(2, SLEEP);
        double result = sleepEventEvaluator.applyAsDouble(new Itinerary(events));

        assertEquals(INCORRECT_SLEEP_PENALTY, result, DELTA);
    }

    private void transformEventAtIndexToType(int index, ActivityType type) {
        Event event = events.get(index);
        Event transformedEvent = new TestEventBuilder()
                .setActivity(new ActivityBuilder()
                        .setType(type)
                        .build())
                .setEventTime(event.getEventTime())
                .build();
        events.remove(event);
        events.add(index, transformedEvent);
    }

    private static Event event(ActivityType type, DateTime startTime, DateTime endTime) {
        return new TestEventBuilder()
                .setActivity(new ActivityBuilder()
                        .setType(type)
                        .build())
                .setEventTime(new Interval(startTime, endTime))
                .build();
    }
}