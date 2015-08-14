package itinerator.datamodel;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.junit.Test;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class WeeklyScheduleTest {
    private static final int MONDAY = 1;
    private static final int TUESDAY = 2;
    private static final int WEDNESDAY = 3;
    private static final int THURSDAY = 4;
    private static final int FRIDAY = 5;
    private static final int SATURDAY = 6;
    private static final int SUNDAY = 7;

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
        return new WeeklySchedule(newArrayList(new WeeklyShift(new WeeklyTimePoint(MONDAY, 9, 0), new WeeklyTimePoint(MONDAY, 17, 0)),
                new WeeklyShift(new WeeklyTimePoint(TUESDAY, 9, 0), new WeeklyTimePoint(TUESDAY, 17, 0)),
                new WeeklyShift(new WeeklyTimePoint(WEDNESDAY, 9, 0), new WeeklyTimePoint(WEDNESDAY, 17, 0)),
                new WeeklyShift(new WeeklyTimePoint(THURSDAY, 9, 0), new WeeklyTimePoint(THURSDAY, 17, 0)),
                new WeeklyShift(new WeeklyTimePoint(FRIDAY, 9, 0), new WeeklyTimePoint(FRIDAY, 17, 0))));
    }

    private static WeeklySchedule twentyFourSeven() {
        return new WeeklySchedule(newArrayList(new WeeklyShift(new WeeklyTimePoint(MONDAY, 0, 0), new WeeklyTimePoint(MONDAY, 0, 0))));
    }

    private static WeeklySchedule sundayToThursdayNights() {
        return new WeeklySchedule(newArrayList(new WeeklyShift(new WeeklyTimePoint(MONDAY, 18, 0), new WeeklyTimePoint(TUESDAY, 2, 0)),
                new WeeklyShift(new WeeklyTimePoint(TUESDAY, 18, 0), new WeeklyTimePoint(WEDNESDAY, 2, 0)),
                new WeeklyShift(new WeeklyTimePoint(WEDNESDAY, 18, 0), new WeeklyTimePoint(THURSDAY, 2, 0)),
                new WeeklyShift(new WeeklyTimePoint(THURSDAY, 18, 0), new WeeklyTimePoint(FRIDAY, 2, 0)),
                new WeeklyShift(new WeeklyTimePoint(SUNDAY, 18, 0), new WeeklyTimePoint(MONDAY, 2, 0))));
    }

    private static Interval createEventInterval(int dayOfWeek, int hourOfDay, int minuteOfHour) {
        DateTime start = new DateTime()
                .withDayOfWeek(dayOfWeek)
                .withHourOfDay(hourOfDay)
                .withMinuteOfHour(minuteOfHour)
                .withSecondOfMinute(0)
                .withMillisOfSecond(0);
        return new Interval(start, start.plusHours(1));
    }
}