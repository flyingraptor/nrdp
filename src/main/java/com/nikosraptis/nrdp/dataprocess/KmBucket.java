package com.nikosraptis.nrdp.dataprocess;

/**
 * Represents a kilometers bucket for the distances of the rides to compare against
 */
public class KmBucket implements Bucket {

    private String name;

    private Integer leftLimit;

    private Integer rightLimit;

    /** Used for order with other buckets */
    private Integer rank;

    public KmBucket(String name, Integer leftLimit, Integer rightLimit, int rank) {
        this.name = name;
        this.leftLimit = leftLimit;
        this.rightLimit = rightLimit;
        this.rank = rank;
    }

    public Integer getRank() {
        return rank;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Integer getLeftLimit() {
        return leftLimit;
    }

    @Override
    public Integer getRightLimit() {
        return rightLimit;
    }

    @Override
    public int compareTo(Bucket other) {
        return this.rank.compareTo(other.getRank());
    }
}