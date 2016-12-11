package com.github.wslotkin.itinerator.generator.calculators;

import com.github.wslotkin.itinerator.generator.datamodel.Activity;
import com.github.wslotkin.itinerator.generator.datamodel.ImmutableActivity;
import com.github.wslotkin.itinerator.generator.datamodel.ImmutableLocation;
import com.github.wslotkin.itinerator.generator.datamodel.Location;
import com.github.wslotkin.itinerator.generator.performance.TestUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class TravelTimeCalculatorTest {

    private static final Location FIRST_LOCATION = ImmutableLocation.of(1.2, 2.3);
    private static final Location SECOND_LOCATION = ImmutableLocation.of(3.4, 4.5);
    private static final Activity FIRST_ACTIVITY = ImmutableActivity.builder().location(FIRST_LOCATION).build();
    private static final Activity SECOND_ACTIVITY = ImmutableActivity.builder().location(SECOND_LOCATION).build();
    private static final double DISTANCE = 5.6;

    private DistanceCalculator distanceCalculator;
    private TravelTimeCalculator travelTimeCalculator;

    @Before
    public void before() {
        distanceCalculator = mock(DistanceCalculator.class);

        when(distanceCalculator.calculate(FIRST_LOCATION, SECOND_LOCATION)).thenReturn(DISTANCE);

        travelTimeCalculator = new TravelTimeCalculator(distanceCalculator);
    }

    @Test
    public void returnsDistanceDividedByTravelSpeed() {
        double result = travelTimeCalculator.calculate(FIRST_ACTIVITY, SECOND_ACTIVITY);

        double expectedResult = DISTANCE / TravelTimeCalculator.SPEED_IN_KM_PER_MINUTE;

        Assert.assertEquals(expectedResult, result, TestUtil.DELTA);

        verify(distanceCalculator).calculate(FIRST_LOCATION, SECOND_LOCATION);
    }
}