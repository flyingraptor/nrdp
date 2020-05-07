package com.nikosraptis.nrdp.dataprocess.ride;

import java.util.List;

import com.nikosraptis.nrdp.dataprocess.DistanceCalculatorStrategy;

/**
 * Responsible to provide an way to client classes to perform distance
 * calculation by decoupling the strategy or algorithm used underline
 */
public class RideDistance {

    private DistanceCalculatorStrategy distanceCalculator;

    public RideDistance(DistanceCalculatorStrategy distanceCalculator) {
        this.distanceCalculator = distanceCalculator;
    }

    public double calculateDistance(Ride ride) {
        return distanceCalculator.sumBetweenListOfPoints((List<RoutePoint>) ride.getRoutePoints());
    }
}