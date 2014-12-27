package itinerator.calculators;

import itinerator.datamodel.Event;
import itinerator.datamodel.Location;

/**
 * Created by smatt989 on 12/26/14.
 */
public class DistanceCalculator {
    public double calculate(Event start, Event end) {
        return Math.abs(haversineFormula(start.getActivity().getLocation(), end.getActivity().getLocation()));
    }

    private double haversineFormula(Location start, Location end) {
        double radius = 6371;
        double startLatitudeRadians = Math.toRadians(start.getLatitude());
        double endLatitudeRadians = Math.toRadians(end.getLatitude());
        double deltaLatitude = Math.toRadians(end.getLatitude() - start.getLatitude());
        double deltaLongitude = Math.toRadians(end.getLongitude() - start.getLongitude());

        double a = Math.pow(Math.sin(deltaLatitude / 2), 2)
                + Math.cos(startLatitudeRadians)
                * Math.cos(endLatitudeRadians)
                * Math.pow(Math.sin(deltaLongitude / 2), 2);


        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return radius * c;
    }
}
