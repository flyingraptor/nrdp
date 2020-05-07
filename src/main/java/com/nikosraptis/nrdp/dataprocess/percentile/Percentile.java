package com.nikosraptis.nrdp.dataprocess.percentile;

import java.util.ArrayList;
import java.util.List;

import com.nikosraptis.nrdp.dataprocess.Bucket;

/**
 * Percentile class to hold the percentile details.
 */
public class Percentile implements Comparable<Percentile> {

    /** The bucket the percentile is linked. E.g a Kilometers Bucket */
    private Bucket linkedBucket;

    private Long value;

    private List<Long> data = new ArrayList<>();

    private PercentileCalculator percentileCalculator = new PercentileCalculator();

    public Percentile(Bucket linkedBucket) {
        this.linkedBucket = linkedBucket;
    }

    public Long getValue() {
        return value;
    }

    /**
     * Receives a value to be inserted to the list that will be used to calucalate
     * the percentile. Finally it calculate the new percentile.
     * 
     * @param dataValue  The value to be inserted
     * @param percentile The target percentile. E.g 95%
     */
    public void addDataAndCalculate(long dataValue, int percentile) {
        data.add(dataValue);
        value = percentileCalculator.percentileOf(data, percentile);
    }

    public List<Long> getData() {
        return data;
    }

    public String getLinkedBucketName() {
        return linkedBucket.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Percentile)) {
            return false;
        }
        Percentile percentile = (Percentile) o;
        return percentile.linkedBucket.equals(linkedBucket);
    }

    @Override
    public int compareTo(Percentile other) {
        return this.linkedBucket.compareTo(other.linkedBucket);
    }
}