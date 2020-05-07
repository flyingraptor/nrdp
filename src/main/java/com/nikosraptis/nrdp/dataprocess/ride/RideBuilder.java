package com.nikosraptis.nrdp.dataprocess.ride;

/**
 * Gives a easier way to create rides and takes care of some type casts
 */
public class RideBuilder {

    private Ride ride = null;

    private RideDistance rideDistance = new RideDistance(new HaversineFormula());

    public RideBuilder() {
    }

    public RideBuilder(Ride ride) {
        this.ride = ride;
    }

    public RideBuilder createNewRide(String id) {
        ride = new Ride();
        ride.setId(Integer.parseInt(id));
        return this;
    }

    public RideBuilder addRoutePoint(String latitude, String longitude, String recordedTimestamp) {
        RoutePoint routePoint = new RoutePoint(Double.parseDouble(latitude), Double.parseDouble(longitude),
                Long.parseLong(recordedTimestamp));
        ride.addPointToRoute(routePoint);
        ride.setDistance(rideDistance.calculateDistance(ride));
        return this;
    }

    public Ride getRide() {
        return this.ride;
    }
}