package com.github.wslotkin.itinerator.generator.calculators;

import com.github.wslotkin.itinerator.generator.TestUtil;
import com.github.wslotkin.itinerator.generator.datamodel.Activity;
import com.github.wslotkin.itinerator.generator.datamodel.ImmutableActivity;
import com.github.wslotkin.itinerator.generator.datamodel.ImmutableLocation;
import com.github.wslotkin.itinerator.generator.datamodel.Location;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class TravelTimeCalculatorTest {

    private static final Location FIRST_LOCATION = ImmutableLocation.of(1.2, 2.3);
    private static final Location SECOND_LOCATION = ImmutableLocation.of(3.4, 4.5);
    private static final Location DISTANT_LOCATION = ImmutableLocation.of(100.1, 200.2);
    private static final Activity FIRST_ACTIVITY = ImmutableActivity.builder().location(FIRST_LOCATION).build();
    private static final Activity SECOND_ACTIVITY = ImmutableActivity.builder().location(SECOND_LOCATION).build();
    private static final Activity DISTANT_ACTIVITY = ImmutableActivity.builder().location(DISTANT_LOCATION).build();
    private static final double DISTANCE = 1.2;
    private static final double FAR_DISTANCE = 100.2;

    private DistanceCalculator distanceCalculator;
    private TravelTimeCalculator travelTimeCalculator;

    @Before
    public void before() {
        distanceCalculator = mock(DistanceCalculator.class);

        when(distanceCalculator.calculate(FIRST_LOCATION, SECOND_LOCATION)).thenReturn(DISTANCE);
        when(distanceCalculator.calculate(FIRST_LOCATION, DISTANT_LOCATION)).thenReturn(FAR_DISTANCE);

        travelTimeCalculator = new TravelTimeCalculator(distanceCalculator);
    }

    @Test
    public void whenDistanceIsWalkableShouldReturnsDistanceDividedByWalkingSpeed() {
        double result = travelTimeCalculator.calculate(FIRST_ACTIVITY, SECOND_ACTIVITY);

        double expectedResult = DISTANCE / TravelTimeCalculator.WALKING_SPEED_IN_KM_PER_MINUTE;

        assertEquals(expectedResult, result, TestUtil.DELTA);

        verify(distanceCalculator).calculate(FIRST_LOCATION, SECOND_LOCATION);
    }

    @Test
    public void whenDistanceIsFarShouldReturnsDistanceDividedByDrivingSpeed() {
        double result = travelTimeCalculator.calculate(FIRST_ACTIVITY, DISTANT_ACTIVITY);

        double expectedResult = FAR_DISTANCE / TravelTimeCalculator.DRIVING_SPEED_IN_KM_PER_MINUTE;

        assertEquals(expectedResult, result, TestUtil.DELTA);

        verify(distanceCalculator).calculate(FIRST_LOCATION, DISTANT_LOCATION);
    }
}