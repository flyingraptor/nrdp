package com.nikosraptis.nrdp.dataprocess;

import java.util.List;
import java.util.Map;

import com.nikosraptis.nrdp.dataprocess.percentile.Percentile;
import com.nikosraptis.nrdp.dataprocess.ride.Ride;

public interface PercentileService {
    <T extends Bucket> void updatePercentiles(Ride ride, BucketRange<T> bucketRange);
    Map<String, List<Percentile>> getPercentilesGroupByTimeslot();
}