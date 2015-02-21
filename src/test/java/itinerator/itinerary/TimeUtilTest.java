package itinerator.itinerary;

import org.joda.time.DateTime;
import org.junit.Test;

import static itinerator.itinerary.TimeUtil.*;
import static org.junit.Assert.*;

public class TimeUtilTest {

    private static final DateTime START_OF_TODAY = DateTime.now().withTimeAtStartOfDay();
    private static final DateTime START_OF_TOMORROW = START_OF_TODAY.plusDays(1);

    @Test
    public void indicatesWhetherTimeIsInSleepWindow() {
        assertFalse(isInSleepWindow(START_OF_BREAKFAST_WINDOW.toDateTimeToday()));
        assertFalse(isInSleepWindow(END_OF_BREAKFAST_WINDOW.toDateTimeToday()));
        assertFalse(isInSleepWindow(START_OF_LUNCH_WINDOW.toDateTimeToday()));
        assertFalse(isInSleepWindow(END_OF_LUNCH_WINDOW.toDateTimeToday()));
        assertFalse(isInSleepWindow(START_OF_DINNER_WINDOW.toDateTimeToday()));
        assertFalse(isInSleepWindow(END_OF_DINNER_WINDOW.toDateTimeToday()));
        assertTrue(isInSleepWindow(START_OF_SLEEP_WINDOW.toDateTimeToday()));
        assertFalse(isInSleepWindow(END_OF_SLEEP_WINDOW.toDateTimeToday()));
        assertTrue(isInSleepWindow(START_OF_TODAY));
    }

    @Test
    public void indicatesWhetherTimeIsInMealWindow() {
        assertTrue(isInMealWindow(START_OF_BREAKFAST_WINDOW.toDateTimeToday()));
        assertFalse(isInMealWindow(END_OF_BREAKFAST_WINDOW.toDateTimeToday()));
        assertTrue(isInMealWindow(START_OF_LUNCH_WINDOW.toDateTimeToday()));
        assertFalse(isInMealWindow(END_OF_LUNCH_WINDOW.toDateTimeToday()));
        assertTrue(isInMealWindow(START_OF_DINNER_WINDOW.toDateTimeToday()));
        assertFalse(isInMealWindow(END_OF_DINNER_WINDOW.toDateTimeToday()));
        assertFalse(isInMealWindow(START_OF_SLEEP_WINDOW.toDateTimeToday()));
        assertTrue(isInMealWindow(END_OF_SLEEP_WINDOW.toDateTimeToday()));
        assertFalse(isInMealWindow(START_OF_TODAY));
    }


    @Test
    public void computesNumberOfMealsBetweenToTimes() {
        assertEquals(0, numberOfMealsInTimeRange(START_OF_TODAY, START_OF_BREAKFAST_WINDOW.toDateTimeToday()));
        assertEquals(1, numberOfMealsInTimeRange(START_OF_BREAKFAST_WINDOW.toDateTimeToday(), END_OF_BREAKFAST_WINDOW.toDateTimeToday()));
        assertEquals(1, numberOfMealsInTimeRange(START_OF_DINNER_WINDOW.toDateTimeToday(), START_OF_TOMORROW));
        assertEquals(2, numberOfMealsInTimeRange(START_OF_LUNCH_WINDOW.toDateTimeToday(), END_OF_DINNER_WINDOW.toDateTimeToday()));
        assertEquals(3, numberOfMealsInTimeRange(START_OF_TODAY, START_OF_TOMORROW));
    }
}