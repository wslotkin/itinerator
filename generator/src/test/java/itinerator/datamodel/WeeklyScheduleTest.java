package itinerator.datamodel;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalTime;
import org.junit.Test;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static itinerator.datamodel.Day.*;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class WeeklyScheduleTest {

    private static final List<Interval> EVENT_TIMES = newArrayList(createEventInterval(MONDAY, 1, 0),
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

        for (Interval eventTime : EVENT_TIMES) {
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

        for (Interval eventTime : EVENT_TIMES) {
            assertTrue(schedule.isValid(eventTime));
        }
    }

    private static WeeklySchedule weekdaysNineToFive() {
        return new WeeklySchedule(newArrayList(new WeeklyShift(new WeeklyTimePoint(MONDAY, new LocalTime(9, 0)), new WeeklyTimePoint(MONDAY, new LocalTime(17, 0))),
                new WeeklyShift(new WeeklyTimePoint(TUESDAY, new LocalTime(9, 0)), new WeeklyTimePoint(TUESDAY, new LocalTime(17, 0))),
                new WeeklyShift(new WeeklyTimePoint(WEDNESDAY, new LocalTime(9, 0)), new WeeklyTimePoint(WEDNESDAY, new LocalTime(17, 0))),
                new WeeklyShift(new WeeklyTimePoint(THURSDAY, new LocalTime(9, 0)), new WeeklyTimePoint(THURSDAY, new LocalTime(17, 0))),
                new WeeklyShift(new WeeklyTimePoint(FRIDAY, new LocalTime(9, 0)), new WeeklyTimePoint(FRIDAY, new LocalTime(17, 0)))));
    }

    private static WeeklySchedule twentyFourSeven() {
        return new WeeklySchedule(newArrayList(new WeeklyShift(new WeeklyTimePoint(MONDAY, new LocalTime(0, 0)), new WeeklyTimePoint(MONDAY, new LocalTime(0, 0)))));
    }

    private static WeeklySchedule sundayToThursdayNights() {
        return new WeeklySchedule(newArrayList(new WeeklyShift(new WeeklyTimePoint(MONDAY, new LocalTime(18, 0)), new WeeklyTimePoint(TUESDAY, new LocalTime(2, 0))),
                new WeeklyShift(new WeeklyTimePoint(TUESDAY, new LocalTime(18, 0)), new WeeklyTimePoint(WEDNESDAY, new LocalTime(2, 0))),
                new WeeklyShift(new WeeklyTimePoint(WEDNESDAY, new LocalTime(18, 0)), new WeeklyTimePoint(THURSDAY, new LocalTime(2, 0))),
                new WeeklyShift(new WeeklyTimePoint(THURSDAY, new LocalTime(18, 0)), new WeeklyTimePoint(FRIDAY, new LocalTime(2, 0))),
                new WeeklyShift(new WeeklyTimePoint(SUNDAY, new LocalTime(18, 0)), new WeeklyTimePoint(MONDAY, new LocalTime(2, 0)))));
    }

    private static Interval createEventInterval(Day dayOfWeek, int hourOfDay, int minuteOfHour) {
        DateTime start = new DateTime()
                .withDayOfWeek(dayOfWeek.getDayOfWeek())
                .withHourOfDay(hourOfDay)
                .withMinuteOfHour(minuteOfHour)
                .withSecondOfMinute(0)
                .withMillisOfSecond(0);
        return new Interval(start, start.plusHours(1));
    }
}