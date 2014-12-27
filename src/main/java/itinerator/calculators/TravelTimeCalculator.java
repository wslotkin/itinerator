package itinerator.calculators;

import itinerator.datamodel.Event;

/**
 * Created by smatt989 on 12/27/14.
 */
public class TravelTimeCalculator {
    public double calculate(Event start, Event end) {
        double rateOfTravel = .5; //km/minute
        double distance = new DistanceCalculator().calculate(start, end);
        return distance * rateOfTravel;

    }
}
