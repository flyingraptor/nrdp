package com.nikosraptis.nrdp.dataprocess;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import com.nikosraptis.nrdp.dataprocess.percentile.Percentile;
import com.nikosraptis.nrdp.dataprocess.ride.Ride;

/**
 * The heart of the program, It sits in the middle and it's not aware of the any
 * Writers or Readers. It read from a Queue and start calculate. Then Observers
 * are notified for any update.
 */
public class ProcessService implements Runnable {

    private BlockingQueue<Ride> ridesQueue;

    private ArrayList<ProcessObserver> observers;

    private PercentileService percentileService;

    private BucketRange<KmBucket> bucketRange;

    private static final int WAIT_IF_EMPTY_TIME = 3;

    /**
     * Constuctor
     * 
     * @param ridesQueue        The Queue to wait for Rides
     * @param percentileService The service to use for percentile calculations
     */
    public ProcessService(BlockingQueue<Ride> ridesQueue, PercentileService percentileService) {
        this.ridesQueue = ridesQueue;
        this.observers = new ArrayList<ProcessObserver>();
        this.percentileService = percentileService;
        createBucketRange();
    }

    /**
     * The thread entry point. it waits for rides in queue and starts uses the
     * percentile service. When it's done it notifies all observers with the
     * results.
     */
    @Override
    public void run() {

        Ride ride = null;
        try {
            while ((ride = ridesQueue.poll(WAIT_IF_EMPTY_TIME, TimeUnit.SECONDS)) != null) {
                percentileService.updatePercentiles(ride, bucketRange);
                final Map<String, List<Percentile>> percentilesByDateTime = percentileService
                        .getPercentilesGroupByTimeslot();
                for (ProcessObserver observer : observers) {
                    for (final Map.Entry<String, List<Percentile>> percentileEntry : percentilesByDateTime.entrySet()) {
                        observer.update(percentileEntry.getKey(), percentileEntry.getValue());
                    }
                }
            }
        } catch (InterruptedException e) {
            throw new ProcessException(e.getMessage());
        }
    }

    /**
     * Used from clients to register process observers.
     * @param observer A process oberver
     */
    public void registerObserver(ProcessObserver observer) {
        observers.add(observer);
    }

    /**
     * Create a kilometers bucket range
     */
    protected void createBucketRange() {
        bucketRange = new BucketRange<KmBucket>();
        bucketRange.addBucket(new KmBucket("1km", 0, 1, 0));
        bucketRange.addBucket(new KmBucket("2km", 1, 2, 1));
        bucketRange.addBucket(new KmBucket("3km", 2, 3, 2));
        bucketRange.addBucket(new KmBucket("5km", 3, 5, 3));
        bucketRange.addBucket(new KmBucket("8km", 5, 8, 4));
        bucketRange.addBucket(new KmBucket("13km", 8, 13, 5));
        bucketRange.addBucket(new KmBucket("21km", 13, 21, 6));
        bucketRange.addBucket(new KmBucket("21+km", 21, null, 7));
    }
}