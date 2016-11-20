package itinerator.datamodel;

import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static java.time.DayOfWeek.*;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class WeeklyScheduleTest {

    @SuppressWarnings("unchecked")
    private static final List<Range<LocalDateTime>> EVENT_TIMES = newArrayList(createEventInterval(MONDAY, 1, 0),
            createEventInterval(MONDAY, 9, 10),
            createEventInterval(MONDAY, 18, 20),
            createEventInterval(TUESDAY, 4, 30),
            createEventInterval(TUESDAY, 13, 40),
            createEventInterval(TUESDAY, 21, 50),
            createEventInterval(WEDNESDAY, 6, 0),
            createEventInterval(WEDNESDAY, 14, 5),
            createEventInterval(WEDNESDAY, 23, 15),
            createEventInterval(THURSDAY, 7, 25),
            createEventInterval(THURSDAY, 12, 35),
            createEventInterval(THURSDAY, 16, 45),
            createEventInterval(FRIDAY, 9, 55),
            createEventInterval(FRIDAY, 14, 0),
            createEventInterval(FRIDAY, 19, 0),
            createEventInterval(SATURDAY, 10, 0),
            createEventInterval(SATURDAY, 15, 0),
            createEventInterval(SATURDAY, 20, 0),
            createEventInterval(SUNDAY, 0, 0),
            createEventInterval(SUNDAY, 11, 0),
            createEventInterval(SUNDAY, 17, 0));

    @Test
    public void weekdaysNineToFiveScheduleShouldCorrectlyMarkEventsValid() {
        WeeklySchedule schedule = weekdaysNineToFive();

        List<Integer> expectedValidEvents = newArrayList(1, 4, 7, 10, 12, 13);

        for (int i = 0; i < EVENT_TIMES.size(); i++) {
            assertEquals(expectedValidEvents.contains(i), schedule.isValid(EVENT_TIMES.get(i)));
        }
    }

    @Test
    public void twentyFourSevenScheduleShouldConsiderAllEventsValid() {
        WeeklySchedule schedule = twentyFourSeven();

        for (Range<LocalDateTime> eventTime : EVENT_TIMES) {
            assertTrue(schedule.isValid(eventTime));
        }
    }

    @Test
    public void sundayToThursdayNightsScheduleShouldCorrectlyMarkEventsValid() {
        WeeklySchedule schedule = sundayToThursdayNights();

        List<Integer> expectedValidEvents = newArrayList(0, 2, 5, 8);

        for (int i = 0; i < EVENT_TIMES.size(); i++) {
            assertEquals(expectedValidEvents.contains(i), schedule.isValid(EVENT_TIMES.get(i)));
        }
    }

    @Test
    public void emptyScheduleShouldConsiderAllEventsValid() {
        WeeklySchedule schedule = new WeeklySchedule(emptyList());

        for (Range<LocalDateTime> eventTime : EVENT_TIMES) {
            assertTrue(schedule.isValid(eventTime));
        }
    }

    private static WeeklySchedule weekdaysNineToFive() {
        return new WeeklySchedule(newArrayList(ImmutableWeeklyShift.of(ImmutableWeeklyTimePoint.of(MONDAY, LocalTime.of(9, 0)), ImmutableWeeklyTimePoint.of(MONDAY, LocalTime.of(17, 0))),
                ImmutableWeeklyShift.of(ImmutableWeeklyTimePoint.of(TUESDAY, LocalTime.of(9, 0)), ImmutableWeeklyTimePoint.of(TUESDAY, LocalTime.of(17, 0))),
                ImmutableWeeklyShift.of(ImmutableWeeklyTimePoint.of(WEDNESDAY, LocalTime.of(9, 0)), ImmutableWeeklyTimePoint.of(WEDNESDAY, LocalTime.of(17, 0))),
                ImmutableWeeklyShift.of(ImmutableWeeklyTimePoint.of(THURSDAY, LocalTime.of(9, 0)), ImmutableWeeklyTimePoint.of(THURSDAY, LocalTime.of(17, 0))),
                ImmutableWeeklyShift.of(ImmutableWeeklyTimePoint.of(FRIDAY, LocalTime.of(9, 0)), ImmutableWeeklyTimePoint.of(FRIDAY, LocalTime.of(17, 0)))));
    }

    private static WeeklySchedule twentyFourSeven() {
        return new WeeklySchedule(newArrayList(ImmutableWeeklyShift.of(ImmutableWeeklyTimePoint.of(MONDAY, LocalTime.of(0, 0)), ImmutableWeeklyTimePoint.of(MONDAY, LocalTime.of(0, 0)))));
    }

    private static WeeklySchedule sundayToThursdayNights() {
        return new WeeklySchedule(newArrayList(ImmutableWeeklyShift.of(ImmutableWeeklyTimePoint.of(MONDAY, LocalTime.of(18, 0)), ImmutableWeeklyTimePoint.of(TUESDAY, LocalTime.of(2, 0))),
                ImmutableWeeklyShift.of(ImmutableWeeklyTimePoint.of(TUESDAY, LocalTime.of(18, 0)), ImmutableWeeklyTimePoint.of(WEDNESDAY, LocalTime.of(2, 0))),
                ImmutableWeeklyShift.of(ImmutableWeeklyTimePoint.of(WEDNESDAY, LocalTime.of(18, 0)), ImmutableWeeklyTimePoint.of(THURSDAY, LocalTime.of(2, 0))),
                ImmutableWeeklyShift.of(ImmutableWeeklyTimePoint.of(THURSDAY, LocalTime.of(18, 0)), ImmutableWeeklyTimePoint.of(FRIDAY, LocalTime.of(2, 0))),
                ImmutableWeeklyShift.of(ImmutableWeeklyTimePoint.of(SUNDAY, LocalTime.of(18, 0)), ImmutableWeeklyTimePoint.of(MONDAY, LocalTime.of(2, 0)))));
    }

    private static Range<LocalDateTime> createEventInterval(DayOfWeek dayOfWeek, int hourOfDay, int minuteOfHour) {
        LocalDateTime start = LocalDateTime.of(LocalDate.now(), java.time.LocalTime.of(hourOfDay, minuteOfHour));
        while (start.getDayOfWeek() != dayOfWeek) {
            start = start.plusDays(1);
        }
        return Range.of(start, start.plusHours(1));
    }
}