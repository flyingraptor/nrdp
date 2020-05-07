package com.nikosraptis.nrdp.dataprocess.ride;

import java.util.List;

import com.nikosraptis.nrdp.dataprocess.DistanceCalculatorStrategy;

/**
 * Responsible to calculate the distance between two or more points using the
 * Haversine Formula
 */
class HaversineFormula implements DistanceCalculatorStrategy {

    /* Approximately the radius of earth */
    private static final int EARTH_RADIUS = 6378;

    /** Used for validating the lat and lng */
    String validLatitudeRegex = "^(\\+|-)?((\\d((\\.)|\\.\\d{1,6})?)|(0*?[0-8]\\d((\\.)|\\.\\d{1,6})?)|(0*?90((\\.)|\\.0{1,6})?))$";
    String validLongitudeRegex = "^(\\+|-)?((\\d((\\.)|\\.\\d{1,6})?)|(0*?\\d\\d((\\.)|\\.\\d{1,6})?)|(0*?1[0-7]\\d((\\.)|\\.\\d{1,6})?)|(0*?180((\\.)|\\.0{1,6})?))$";

    /**
     * Calculates the distance between two points.
     * 
     * @param startPoint The start point
     * @param endPoint   The end point.
     */
    public double betweenTwoPoints(RoutePoint startPoint, RoutePoint endPoint) {

        validate(startPoint, endPoint);

        double dLat = Math.toRadians((endPoint.getLatitude() - startPoint.getLatitude()));
        double dLong = Math.toRadians((endPoint.getLongitude() - startPoint.getLongitude()));

        double startLat = Math.toRadians(startPoint.getLatitude());
        double endLat = Math.toRadians(endPoint.getLatitude());

        double a = haversin(dLat) + Math.cos(startLat) * Math.cos(endLat) * haversin(dLong);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }

    /**
     * Calculates the total distance of a list of points connected to each other.
     * 
     * @param points The interconnected points.
     */
    @Override
    public double sumBetweenListOfPoints(List<RoutePoint> points) {

        double sumOfDistances = 0;

        if (points == null || points.size() == 0) {
            return sumOfDistances;
        }

        for (int i = 0; i < points.size() - 1; i++) {
            sumOfDistances += betweenTwoPoints(points.get(i), points.get(i + 1));
        }
        return sumOfDistances;
    }

    /**
     * The haversin
     * 
     * @param value to calculate
     * @return
     */
    private static double haversin(double val) {
        return Math.pow(Math.sin(val / 2), 2);
    }

    /**
     * Validation of lat and lng
     */
    private void validate(RoutePoint startPoint, RoutePoint endPoint) {

        if (startPoint == null || endPoint == null) {
            throw new HaversineFormulaException("Error: RoutePoint null");
        }

        boolean validFormat = (String.valueOf(startPoint.getLatitude()).matches(validLatitudeRegex));
        if (!validFormat) {
            throw new HaversineFormulaException("Invalid latitude: " + startPoint.getLatitude());
        }

        validFormat = String.valueOf(startPoint.getLongitude()).matches(validLongitudeRegex);
        if (!validFormat) {
            throw new HaversineFormulaException("Invalid longitude: " + startPoint.getLongitude());
        }

        validFormat = String.valueOf(endPoint.getLatitude()).matches(validLatitudeRegex);
        if (!validFormat) {
            throw new HaversineFormulaException("Invalid latitude: " + endPoint.getLatitude());
        }

        validFormat = String.valueOf(endPoint.getLongitude()).matches(validLongitudeRegex);
        if (!validFormat) {
            throw new HaversineFormulaException("Invalid longitude: " + endPoint.getLongitude());
        }
    }
}