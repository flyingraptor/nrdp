package com.nikosraptis.nrdp.dataprocess;

import java.util.List;

import com.nikosraptis.nrdp.dataprocess.ride.RoutePoint;

/** 
 * Should be implemented for any distance calculator algorithm 
 */
public interface DistanceCalculatorStrategy {
    double betweenTwoPoints(RoutePoint startPoint, RoutePoint endPoint);
    double sumBetweenListOfPoints(List<RoutePoint> points);
}