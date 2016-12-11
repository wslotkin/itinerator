package com.github.wslotkin.itinerator.generator.calculators;

import com.github.wslotkin.itinerator.generator.datamodel.Activity;
import com.google.common.annotations.VisibleForTesting;

public class TravelTimeCalculator {

    @VisibleForTesting
    static final double SPEED_IN_KM_PER_MINUTE = .5;

    private final DistanceCalculator distanceCalculator;

    public TravelTimeCalculator(DistanceCalculator distanceCalculator) {
        this.distanceCalculator = distanceCalculator;
    }

    public double calculate(Activity start, Activity end) {
        double distance = distanceCalculator.calculate(start.getLocation(), end.getLocation());
        return distance / SPEED_IN_KM_PER_MINUTE;
    }
}
