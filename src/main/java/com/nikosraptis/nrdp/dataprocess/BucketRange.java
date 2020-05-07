package com.nikosraptis.nrdp.dataprocess;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Represents an iterable of bucket.
 * @param <T>
 */
public class BucketRange<T extends Bucket> implements Iterable<T> {

    private List<T> buckets = new ArrayList<>();

    @Override
    public Iterator<T> iterator() {
        return buckets.iterator();
    }

    public void addBucket(T bucket) {
        this.buckets.add(bucket);
    }
}