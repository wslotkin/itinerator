package com.github.wslotkin.itinerator.generator.calculators;

import com.github.wslotkin.itinerator.generator.datamodel.ImmutableLocation;
import com.github.wslotkin.itinerator.generator.datamodel.Location;
import org.junit.Before;
import org.junit.Test;

import static com.github.wslotkin.itinerator.generator.performance.TestUtil.DELTA;
import static org.junit.Assert.assertEquals;

public class DistanceCalculatorTest {

    private static final Location START_LOCATION = ImmutableLocation.of(40.775525, -73.961227);
    private static final Location END_LOCATION = ImmutableLocation.of(41.122106, -73.372485);

    private DistanceCalculator distanceCalculator;

    @Before
    public void before() {
        distanceCalculator = new DistanceCalculator();
    }

    @Test
    public void test() {
        double result = distanceCalculator.calculate(START_LOCATION, END_LOCATION);

        assertEquals(62.68975637782323, result, DELTA);
    }
}