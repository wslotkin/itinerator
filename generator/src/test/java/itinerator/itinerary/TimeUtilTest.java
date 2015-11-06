package itinerator.itinerary;

import itinerator.datamodel.Range;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static itinerator.itinerary.TimeUtil.*;
import static org.junit.Assert.*;

public class TimeUtilTest {

    private static final LocalDate TODAY = LocalDate.now();
    private static final LocalDateTime START_OF_TODAY = LocalDateTime.of(TODAY, LocalTime.of(0, 0));
    private static final LocalDateTime START_OF_TOMORROW = START_OF_TODAY.plusDays(1);

    @Test
    public void indicatesWhetherTimeIsInSleepWindow() {
        assertTrue(isInSleepWindow(BREAKFAST_WINDOW.getStart().atDate(TODAY)));
        assertFalse(isInSleepWindow(BREAKFAST_WINDOW.getEnd().atDate(TODAY)));
        assertFalse(isInSleepWindow(LUNCH_WINDOW.getStart().atDate(TODAY)));
        assertFalse(isInSleepWindow(LUNCH_WINDOW.getEnd().atDate(TODAY)));
        assertFalse(isInSleepWindow(DINNER_WINDOW.getStart().atDate(TODAY)));
        assertFalse(isInSleepWindow(DINNER_WINDOW.getEnd().atDate(TODAY)));
        assertTrue(isInSleepWindow(SLEEP_WINDOW.getStart().atDate(TODAY)));
        assertTrue(isInSleepWindow(SLEEP_WINDOW.getEnd().atDate(TODAY)));
        assertTrue(isInSleepWindow(START_OF_TODAY));
    }

    @Test
    public void indicatesWhetherTimeIsInMealWindow() {
        assertTrue(isInMealWindow(BREAKFAST_WINDOW.getStart().atDate(TODAY)));
        assertTrue(isInMealWindow(BREAKFAST_WINDOW.getEnd().atDate(TODAY)));
        assertTrue(isInMealWindow(LUNCH_WINDOW.getStart().atDate(TODAY)));
        assertTrue(isInMealWindow(LUNCH_WINDOW.getEnd().atDate(TODAY)));
        assertFalse(isInMealWindow(LUNCH_WINDOW.getEnd().plusHours(1).atDate(TODAY)));
        assertTrue(isInMealWindow(DINNER_WINDOW.getStart().atDate(TODAY)));
        assertTrue(isInMealWindow(DINNER_WINDOW.getEnd().atDate(TODAY)));
        assertFalse(isInMealWindow(SLEEP_WINDOW.getStart().atDate(TODAY)));
        assertTrue(isInMealWindow(SLEEP_WINDOW.getEnd().atDate(TODAY)));
        assertFalse(isInMealWindow(START_OF_TODAY));
    }


    @Test
    public void computesNumberOfMealsBetweenToTimes() {
        assertEquals(0, numberOfMealsInTimeRange(Range.of(START_OF_TODAY, BREAKFAST_WINDOW.getStart().atDate(TODAY))));
        assertEquals(1, numberOfMealsInTimeRange(Range.of(BREAKFAST_WINDOW.getStart().atDate(TODAY), BREAKFAST_WINDOW.getEnd().atDate(TODAY))));
        assertEquals(1, numberOfMealsInTimeRange(Range.of(DINNER_WINDOW.getStart().atDate(TODAY), START_OF_TOMORROW)));
        assertEquals(2, numberOfMealsInTimeRange(Range.of(LUNCH_WINDOW.getStart().atDate(TODAY), DINNER_WINDOW.getEnd().atDate(TODAY))));
        assertEquals(3, numberOfMealsInTimeRange(Range.of(START_OF_TODAY, START_OF_TOMORROW)));
    }
}