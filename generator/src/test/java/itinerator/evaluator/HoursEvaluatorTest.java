package itinerator.evaluator;

import itinerator.datamodel.*;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.google.common.collect.Lists.newArrayList;
import static itinerator.TestUtil.DELTA;
import static itinerator.evaluator.EventEvaluators.hoursEvaluator;
import static java.time.DayOfWeek.MONDAY;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;

public class HoursEvaluatorTest {
    private static final double INVALID_HOURS_PENALTY = -60.0;
    private static final WeeklySchedule DEFAULT_SCHEDULE = new WeeklySchedule(newArrayList(ImmutableWeeklyShift.of(
            ImmutableWeeklyTimePoint.of(MONDAY, LocalTime.of(9, 0)),
            ImmutableWeeklyTimePoint.of(MONDAY, LocalTime.of(17, 0)))));

    private Evaluator<Itinerary> hoursEvaluator;

    @Before
    public void before() {
        hoursEvaluator = new EventEvaluators(hoursEvaluator(INVALID_HOURS_PENALTY));
    }

    @Test
    public void whenItineraryIsEmptyShouldReturnZero() {
        double result = hoursEvaluator.applyAsDouble(ImmutableItinerary.of(emptyList()));

        assertEquals(0.0, result, DELTA);
    }

    @Test
    public void shouldReturnNumberOfInvalidEventsMultipliedByPenalty() {
        double result = hoursEvaluator.applyAsDouble(ImmutableItinerary.of(newArrayList(createEventWithStart(7),
                createEventWithStart(8),
                createEventWithStart(10),
                createEventWithStart(11))));

        double expectedResult = INVALID_HOURS_PENALTY * 2;

        assertEquals(expectedResult, result, DELTA);
    }

    private static Event createEventWithStart(int hourOfDay) {
        LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.of(hourOfDay, 0));
        while (start.getDayOfWeek() != MONDAY) {
            start = start.plusDays(1);
        }

        return ImmutableEvent.builder()
                .eventTime(Range.of(start, start.plusHours(1)))
                .activity(ImmutableActivity.builder().weeklySchedule(DEFAULT_SCHEDULE).build())
                .build();
    }
}