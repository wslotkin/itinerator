package com.github.wslotkin.itinerator.generator.evaluator;

import com.github.wslotkin.itinerator.generator.datamodel.*;
import com.github.wslotkin.itinerator.generator.performance.TestUtil;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.google.common.collect.Lists.newArrayList;
import static java.time.DayOfWeek.MONDAY;
import static java.util.Collections.emptyList;

public class HoursEvaluatorTest {
    private static final double INVALID_HOURS_PENALTY = -60.0;
    private static final WeeklySchedule DEFAULT_SCHEDULE = new WeeklySchedule(Lists.newArrayList(ImmutableWeeklyShift.of(
            ImmutableWeeklyTimePoint.of(MONDAY, LocalTime.of(9, 0)),
            ImmutableWeeklyTimePoint.of(MONDAY, LocalTime.of(17, 0)))));

    private Evaluator<Itinerary> hoursEvaluator;

    @Before
    public void before() {
        hoursEvaluator = new EventEvaluators(EventEvaluators.hoursEvaluator(INVALID_HOURS_PENALTY));
    }

    @Test
    public void whenItineraryIsEmptyShouldReturnZero() {
        double result = hoursEvaluator.applyAsDouble(ImmutableItinerary.of(emptyList()));

        Assert.assertEquals(0.0, result, TestUtil.DELTA);
    }

    @Test
    public void shouldReturnNumberOfInvalidEventsMultipliedByPenalty() {
        double result = hoursEvaluator.applyAsDouble(ImmutableItinerary.of(newArrayList(createEventWithStart(7),
                createEventWithStart(8),
                createEventWithStart(10),
                createEventWithStart(11))));

        double expectedResult = INVALID_HOURS_PENALTY * 2;

        Assert.assertEquals(expectedResult, result, TestUtil.DELTA);
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