package itinerator.calculators;

import itinerator.datamodel.Activity;
import itinerator.datamodel.ActivityBuilder;
import org.junit.Before;
import org.junit.Test;

import static itinerator.TestConstants.DELTA;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RoundingTravelTimeCalculatorTest {
    private static final Activity FIRST_EVENT = new ActivityBuilder().setId("firstEvent").build();
    private static final Activity SECOND_EVENT = new ActivityBuilder().setId("secondEvent").build();

    private TravelTimeCalculator calculator;
    private RoundingTravelTimeCalculator roundingCalculator;

    @Before
    public void before() {
        calculator = mock(TravelTimeCalculator.class);
        roundingCalculator = new RoundingTravelTimeCalculator(calculator, 5.0);
    }

    @Test
    public void whenResultIsAlreadyRoundedShouldReturnResultUnchanged() {
        double originalResult = 10.0;
        when(calculator.calculate(FIRST_EVENT, SECOND_EVENT)).thenReturn(originalResult);

        double result = roundingCalculator.calculate(FIRST_EVENT, SECOND_EVENT);

        assertEquals(originalResult, result, DELTA);
    }

    @Test
    public void whenResultIsUnroundedShouldReturnResultRounded() {
        double originalResult = 12.0;
        when(calculator.calculate(FIRST_EVENT, SECOND_EVENT)).thenReturn(originalResult);

        double result = roundingCalculator.calculate(FIRST_EVENT, SECOND_EVENT);

        assertEquals(15.0, result, DELTA);
    }

    @Test
    public void decimalValuesShouldGetRoundedUp() {
        double originalResult = 10.1;
        when(calculator.calculate(FIRST_EVENT, SECOND_EVENT)).thenReturn(originalResult);

        double result = roundingCalculator.calculate(FIRST_EVENT, SECOND_EVENT);

        assertEquals(15.0, result, DELTA);
    }
}