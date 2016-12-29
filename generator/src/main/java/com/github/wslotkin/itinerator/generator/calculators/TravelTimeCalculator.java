package com.github.wslotkin.itinerator.generator.calculators;

import com.github.wslotkin.itinerator.generator.datamodel.Activity;
import com.google.common.annotations.VisibleForTesting;

public class TravelTimeCalculator {

    @VisibleForTesting
    static final double WALKING_SPEED_IN_KM_PER_MINUTE = .1;
    @VisibleForTesting
    static final double DRIVING_SPEED_IN_KM_PER_MINUTE = .5;
    private static final double WALKING_THRESHOLD = 2;

    private final DistanceCalculator distanceCalculator;

    public TravelTimeCalculator(DistanceCalculator distanceCalculator) {
        this.distanceCalculator = distanceCalculator;
    }

    public double calculate(Activity start, Activity end) {
        double distance = distanceCalculator.calculate(start.getLocation(), end.getLocation());
        return distance <= WALKING_THRESHOLD
                ? distance / WALKING_SPEED_IN_KM_PER_MINUTE
                : distance / DRIVING_SPEED_IN_KM_PER_MINUTE;
    }
}
