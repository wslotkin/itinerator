package com.github.wslotkin.itinerator.generator.calculators;

import com.github.wslotkin.itinerator.generator.datamodel.Activity;
import com.github.wslotkin.itinerator.generator.datamodel.ImmutableActivity;
import com.github.wslotkin.itinerator.generator.performance.TestUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RoundingTravelTimeCalculatorTest {
    private static final Activity FIRST_EVENT = ImmutableActivity.builder().id("firstEvent").build();
    private static final Activity SECOND_EVENT = ImmutableActivity.builder().id("secondEvent").build();

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

        Assert.assertEquals(originalResult, result, TestUtil.DELTA);
    }

    @Test
    public void whenResultIsUnroundedShouldReturnResultRounded() {
        double originalResult = 12.0;
        when(calculator.calculate(FIRST_EVENT, SECOND_EVENT)).thenReturn(originalResult);

        double result = roundingCalculator.calculate(FIRST_EVENT, SECOND_EVENT);

        Assert.assertEquals(15.0, result, TestUtil.DELTA);
    }

    @Test
    public void decimalValuesShouldGetRoundedUp() {
        double originalResult = 10.1;
        when(calculator.calculate(FIRST_EVENT, SECOND_EVENT)).thenReturn(originalResult);

        double result = roundingCalculator.calculate(FIRST_EVENT, SECOND_EVENT);

        Assert.assertEquals(15.0, result, TestUtil.DELTA);
    }
}