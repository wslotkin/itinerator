package itinerator.calculators;

import itinerator.datamodel.Location;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DistanceCalculatorTest {

    private static final Location START_LOCATION = new Location(40.775525, -73.961227);
    private static final Location END_LOCATION = new Location(41.122106, -73.372485);

    private DistanceCalculator distanceCalculator;

    @Before
    public void before() {
        distanceCalculator = new DistanceCalculator();
    }

    @Test
    public void test() {
        double result = distanceCalculator.calculate(START_LOCATION, END_LOCATION);

        assertEquals(62.68975637782323, result, 1e-9);
    }
}