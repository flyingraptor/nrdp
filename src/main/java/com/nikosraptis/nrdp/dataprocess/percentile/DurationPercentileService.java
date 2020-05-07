package com.nikosraptis.nrdp.dataprocess.percentile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nikosraptis.nrdp.dataprocess.Bucket;
import com.nikosraptis.nrdp.dataprocess.BucketRange;
import com.nikosraptis.nrdp.dataprocess.PercentileService;
import com.nikosraptis.nrdp.dataprocess.ride.Ride;

/**
 * Responsible for making the core calculation for the durations and distances.
 * Holds and build a structure of percentiles per timeslot. The class receives
 * the rides and updates the current calculations for percentiles regarding to
 * these rides and distances and durations.
 */
public class DurationPercentileService implements PercentileService {

    private int percentile;

    private Map<String, List<Percentile>> percentilesGroupedByTimeslot = new HashMap<>();

    private GROUP_BY_MODE groupByMode;

    public enum GROUP_BY_MODE {
        DAYS_AND_HOURS, HOURS
    }

    /**
     * Constructor
     * 
     * @param percentile  The target percentile. e.g 95%
     * @param groupByMode
     */
    public DurationPercentileService(int percentile, GROUP_BY_MODE groupByMode) {
        this.percentile = percentile;
        this.groupByMode = groupByMode;
    }

    /**
     * The core method that receives a ride and a range of bucket to compare against
     * its distances and durations.
     * 
     * @param ride        The ride to use to update the internal structure for
     *                    timeslots, distances and durations
     * @param bucketRange The distances range to compare the ride distance
     */
    @Override
    public <T extends Bucket> void updatePercentiles(Ride ride, BucketRange<T> bucketRange) {

        for (Bucket bucket : bucketRange) {

            if ((!isDistanceInRange(ride.getDistance(), bucket.getLeftLimit(), bucket.getRightLimit()))) {
                continue;
            }

            String timeslotThatFallsIn = getDateAndHourRideFallsIn(ride);

            List<Percentile> durationPercentiles = null;
            Percentile durationPercentile = null;

            if (percentilesGroupedByTimeslot.get(timeslotThatFallsIn) != null) {
                durationPercentiles = percentilesGroupedByTimeslot.get(timeslotThatFallsIn);

                if (durationPercentiles.indexOf(new Percentile(bucket)) == -1) {
                    durationPercentile = new Percentile(bucket);
                    durationPercentiles.add(durationPercentile);
                } else {
                    durationPercentile = durationPercentiles.get(durationPercentiles.indexOf(new Percentile(bucket)));
                }
            } else {
                durationPercentiles = new ArrayList<>();
                durationPercentile = new Percentile(bucket);
                durationPercentiles.add(durationPercentile);
                percentilesGroupedByTimeslot.put(timeslotThatFallsIn, durationPercentiles);
            }
            durationPercentile.addDataAndCalculate(ride.getRideDuration(), percentile);
        }
    }

    /**
     * Exposes the structure that keeps the calculations.
     * 
     * @return Map<String, List<Percentile>> The that has timeslots and perceniles.
     *         The percentiles are linked with a bucket obcect stored inside.
     */
    @Override
    public Map<String, List<Percentile>> getPercentilesGroupByTimeslot() {
        return percentilesGroupedByTimeslot;
    }

    /**
     * Compare the ride distance if it is between the "start" and "end" limit of the
     * kilometers bucket.
     * 
     * @param distance        The Ride distance
     * @param bucketStartInKm Km start limit
     * @param bucketEndInKm   Km end limit
     * @return boolean True if the distance is in range
     */
    protected boolean isDistanceInRange(final Double distance, Integer bucketStartInKm, Integer bucketEndInKm) {
        boolean isDistanceInRange = false;
        if (bucketEndInKm != null) {
            isDistanceInRange = (bucketStartInKm <= distance && distance < bucketEndInKm);
        } else {
            isDistanceInRange = (bucketStartInKm < distance);
        }
        return isDistanceInRange;
    }

    /**
     * Gets a ride and check the start time. Then it cuts the minutes and seconds
     * and keeps the hours
     * 
     * @param ride The ride
     * @return String The hour (with date or not) the ride start falls.
     */
    protected String getDateAndHourRideFallsIn(Ride ride) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(ride.getStartTimestamp() * 1000);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return getDateAndHourFormat().format(calendar.getTime());
    }


    private SimpleDateFormat getDateAndHourFormat() {
        SimpleDateFormat format = null;
        switch (groupByMode) {
            case DAYS_AND_HOURS: {
                format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                break;
            }
            case HOURS: {
                format = new SimpleDateFormat("HH:mm");
                break;
            }
        }
        return format;
    }
}