package com.github.wslotkin.itinerator.generator.calculators;

import com.github.wslotkin.itinerator.generator.datamodel.Activity;
import com.google.common.annotations.VisibleForTesting;

public class RoundingTravelTimeCalculator {
    private final TravelTimeCalculator calculator;
    private final double timeIncrement;

    public static RoundingTravelTimeCalculator roundingTravelTimeCalculator() {
        return new RoundingTravelTimeCalculator(new TravelTimeCalculator(new DistanceCalculator()), 5.0);
    }

    @VisibleForTesting
    RoundingTravelTimeCalculator(TravelTimeCalculator calculator, double timeIncrement) {
        this.calculator = calculator;
        this.timeIncrement = timeIncrement;
    }

    public double calculate(Activity start, Activity end) {
        double unroundedResult = calculator.calculate(start, end);
        double roundedUpResult = Math.ceil(unroundedResult);
        double increment = (timeIncrement - (roundedUpResult % timeIncrement)) % timeIncrement;
        return (int) (roundedUpResult + increment);
    }
}
