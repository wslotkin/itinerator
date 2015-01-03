package itinerator.calculators;

import itinerator.datamodel.Activity;

public class TravelTimeCalculator {

    private final DistanceCalculator distanceCalculator;

    public TravelTimeCalculator(DistanceCalculator distanceCalculator) {
        this.distanceCalculator = distanceCalculator;
    }

    public double calculate(Activity start, Activity end) {
        double rateOfTravel = .5; //km/minute
        double distance = distanceCalculator.calculate(start.getLocation(), end.getLocation());
        return distance / rateOfTravel;
    }
}
