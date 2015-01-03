package itinerator.calculators;

import itinerator.datamodel.Location;

public class DistanceCalculator {

    private static final double RADIUS_OF_EARTH_IN_KM = 6371.0;

    public double calculate(Location start, Location end) {
        double startLatitudeRadians = Math.toRadians(start.getLatitude());
        double endLatitudeRadians = Math.toRadians(end.getLatitude());
        double deltaLatitude = Math.toRadians(end.getLatitude() - start.getLatitude());
        double deltaLongitude = Math.toRadians(end.getLongitude() - start.getLongitude());

        double a = Math.pow(Math.sin(deltaLatitude / 2), 2)
                + Math.cos(startLatitudeRadians)
                * Math.cos(endLatitudeRadians)
                * Math.pow(Math.sin(deltaLongitude / 2), 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return Math.abs(RADIUS_OF_EARTH_IN_KM * c);
    }
}
