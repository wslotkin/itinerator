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
        assertFalse(isInSleepWindow(START_OF_BREAKFAST_WINDOW.atDate(TODAY)));
        assertFalse(isInSleepWindow(END_OF_BREAKFAST_WINDOW.atDate(TODAY)));
        assertFalse(isInSleepWindow(START_OF_LUNCH_WINDOW.atDate(TODAY)));
        assertFalse(isInSleepWindow(END_OF_LUNCH_WINDOW.atDate(TODAY)));
        assertFalse(isInSleepWindow(START_OF_DINNER_WINDOW.atDate(TODAY)));
        assertFalse(isInSleepWindow(END_OF_DINNER_WINDOW.atDate(TODAY)));
        assertTrue(isInSleepWindow(START_OF_SLEEP_WINDOW.atDate(TODAY)));
        assertFalse(isInSleepWindow(END_OF_SLEEP_WINDOW.atDate(TODAY)));
        assertTrue(isInSleepWindow(START_OF_TODAY));
    }

    @Test
    public void indicatesWhetherTimeIsInMealWindow() {
        assertTrue(isInMealWindow(START_OF_BREAKFAST_WINDOW.atDate(TODAY)));
        assertFalse(isInMealWindow(END_OF_BREAKFAST_WINDOW.atDate(TODAY)));
        assertTrue(isInMealWindow(START_OF_LUNCH_WINDOW.atDate(TODAY)));
        assertFalse(isInMealWindow(END_OF_LUNCH_WINDOW.atDate(TODAY)));
        assertTrue(isInMealWindow(START_OF_DINNER_WINDOW.atDate(TODAY)));
        assertFalse(isInMealWindow(END_OF_DINNER_WINDOW.atDate(TODAY)));
        assertFalse(isInMealWindow(START_OF_SLEEP_WINDOW.atDate(TODAY)));
        assertTrue(isInMealWindow(END_OF_SLEEP_WINDOW.atDate(TODAY)));
        assertFalse(isInMealWindow(START_OF_TODAY));
    }


    @Test
    public void computesNumberOfMealsBetweenToTimes() {
        assertEquals(0, numberOfMealsInTimeRange(new Range<>(START_OF_TODAY, START_OF_BREAKFAST_WINDOW.atDate(TODAY))));
        assertEquals(1, numberOfMealsInTimeRange(new Range<>(START_OF_BREAKFAST_WINDOW.atDate(TODAY), END_OF_BREAKFAST_WINDOW.atDate(TODAY))));
        assertEquals(1, numberOfMealsInTimeRange(new Range<>(START_OF_DINNER_WINDOW.atDate(TODAY), START_OF_TOMORROW)));
        assertEquals(2, numberOfMealsInTimeRange(new Range<>(START_OF_LUNCH_WINDOW.atDate(TODAY), END_OF_DINNER_WINDOW.atDate(TODAY))));
        assertEquals(3, numberOfMealsInTimeRange(new Range<>(START_OF_TODAY, START_OF_TOMORROW)));
    }
}