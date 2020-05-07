package com.nikosraptis.nrdp.dataprocess.ride;

public class RoutePoint {

    private double latitude;

    private double longitude;

    private Long timestamp; /** The time the point was captured */

    public RoutePoint(double latitude, double longitude, Long timestamp) {
        this.setLatitude(latitude);
        this.setLongitude(longitude);
        this.setTimestamp(timestamp);
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}