package com.nikosraptis.nrdp.dataprocess.ride;

import java.util.ArrayList;
import java.util.List;

/**
 * Responbile to keep all data related to a ride in the specific context
 */
public class Ride {

    private int id;

    private List<RoutePoint> routePoints = new ArrayList<>();

    private long startTimestamp;

    private long endTimestamp;

    private Double distance;

    public long getRideDuration() {
        return (getEndTimestamp() - getStartTimestamp());
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getStartTimestamp() {
        if (routePoints.size() > 0) {
            startTimestamp = routePoints.get(0).getTimestamp();
        }
        return startTimestamp;
    }

    public long getEndTimestamp() {

        if (routePoints.size() >= 1) {
            endTimestamp = routePoints.get(routePoints.size() - 1).getTimestamp();
        }
        return endTimestamp;
    }

    public void setEndTimestamp(long endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    public void addPointToRoute(RoutePoint routePoint) {
        this.routePoints.add(routePoint);
    }

    public List<RoutePoint> getRoutePoints() {
        return this.routePoints;
    }
}