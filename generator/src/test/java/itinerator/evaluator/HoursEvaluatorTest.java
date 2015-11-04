package itinerator.evaluator;

import itinerator.datamodel.*;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Test;

import static com.google.common.collect.Lists.newArrayList;
import static itinerator.TestUtil.DELTA;
import static itinerator.datamodel.Day.MONDAY;
import static itinerator.evaluator.EventEvaluators.hoursEvaluator;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;

public class HoursEvaluatorTest {
    private static final double INVALID_HOURS_PENALTY = -60.0;
    private static final WeeklySchedule DEFAULT_SCHEDULE = new WeeklySchedule(newArrayList(new WeeklyShift(
            new WeeklyTimePoint(MONDAY, new LocalTime(9, 0)),
            new WeeklyTimePoint(MONDAY, new LocalTime(17, 0)))));

    private Evaluator<Itinerary> hoursEvaluator;

    @Before
    public void before() {
        hoursEvaluator = new EventEvaluators(hoursEvaluator(INVALID_HOURS_PENALTY));
    }

    @Test
    public void whenItineraryIsEmptyShouldReturnZero() {
        double result = hoursEvaluator.applyAsDouble(new Itinerary(emptyList()));

        assertEquals(0.0, result, DELTA);
    }

    @Test
    public void shouldReturnNumberOfInvalidEventsMultipliedByPenalty() {
        double result = hoursEvaluator.applyAsDouble(new Itinerary(newArrayList(createEventWithStart(7),
                createEventWithStart(8),
                createEventWithStart(10),
                createEventWithStart(11))));

        double expectedResult = INVALID_HOURS_PENALTY * 2;

        assertEquals(expectedResult, result, DELTA);
    }

    private static Event createEventWithStart(int hourOfDay) {
        DateTime start = new DateTime()
                .withDayOfWeek(MONDAY.getDayOfWeek())
                .withTime(hourOfDay, 0, 0, 0);
        return new TestEventBuilder()
                .setEventTime(new Interval(start, start.plusHours(1)))
                .setActivity(new ActivityBuilder().setWeeklySchedule(DEFAULT_SCHEDULE).build())
                .build();
    }
}