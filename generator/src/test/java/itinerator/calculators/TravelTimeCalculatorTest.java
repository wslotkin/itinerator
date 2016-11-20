package itinerator.calculators;

import itinerator.datamodel.Activity;
import itinerator.datamodel.ImmutableActivity;
import itinerator.datamodel.ImmutableLocation;
import itinerator.datamodel.Location;
import org.junit.Before;
import org.junit.Test;

import static itinerator.TestUtil.DELTA;
import static org.junit.Assert.assertEquals;
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

        assertEquals(expectedResult, result, DELTA);

        verify(distanceCalculator).calculate(FIRST_LOCATION, SECOND_LOCATION);
    }
}