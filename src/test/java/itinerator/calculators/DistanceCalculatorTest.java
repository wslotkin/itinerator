package itinerator.calculators;

import itinerator.datamodel.Activity;
import itinerator.datamodel.Event;
import itinerator.datamodel.Location;
import org.joda.time.Interval;
import org.junit.Assert;
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
        double result = distanceCalculator.calculate(createEventWithLocation(START_LOCATION), createEventWithLocation(END_LOCATION));

        assertEquals(62.68975637782323, result, 1e-9);
    }

    private Event createEventWithLocation(Location location) {
        return new Event(new Activity("", 1l, location, 0.0, 0.0), new Interval(0l, 1l));
    }
}