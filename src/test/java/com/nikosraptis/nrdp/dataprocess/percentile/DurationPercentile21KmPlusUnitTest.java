package com.nikosraptis.nrdp.dataprocess.percentile;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nikosraptis.nrdp.dataprocess.BucketRange;
import com.nikosraptis.nrdp.dataprocess.KmBucket;
import com.nikosraptis.nrdp.dataprocess.PercentileService;
import com.nikosraptis.nrdp.dataprocess.percentile.DurationPercentileService.GROUP_BY_MODE;
import com.nikosraptis.nrdp.dataprocess.ride.Ride;
import com.nikosraptis.nrdp.dataprocess.ride.RideBuilder;
import static org.hamcrest.CoreMatchers.is;

import org.hamcrest.collection.IsMapContaining;
import org.junit.Test;

public class DurationPercentile21KmPlusUnitTest {
    
    private PercentileService classUnderTest = null;

    @Test
    public void testPercentiles1km95PercentAndGroupByHours() {

        /* Preparation */
        classUnderTest = new DurationPercentileService(95, GROUP_BY_MODE.HOURS);
        List<Ride> rides = prepare21PluskmRidesWith1to5MinutesDuration();
        BucketRange<KmBucket> bucketRange = createBucketRange();
        
        /* Execution */
        for(Ride ride : rides) {
            classUnderTest.updatePercentiles(ride, bucketRange);
        }

        /* Validation */
        Map<String, List<Percentile>> percentilesPerHours = classUnderTest.getPercentilesGroupByTimeslot();
        assertNotNull(percentilesPerHours);
        assertThat(percentilesPerHours.size(), is(2));
        assertThat(percentilesPerHours, IsMapContaining.hasKey("13:00"));
        assertThat(percentilesPerHours, IsMapContaining.hasKey("14:00"));

        for(Map.Entry<String, List<Percentile>> entry: percentilesPerHours.entrySet()) {
            if(entry.getKey().equals("13:00")) {
                assertThat(entry.getValue().size(), is(1)); // Check List<Percentile> size - must be 1 for this test
                assertThat(entry.getValue().get(0).getValue(), is(300L)); //300 is ~5min is the diff between two epoch times
                assertThat(entry.getValue().get(0).getLinkedBucketName(),is("21+km"));
            }
            if(entry.getKey().equals("14:00")) {
                assertThat(entry.getValue().size(), is(1)); // Check List<Percentile> size - must be 1 for this test
                assertThat(entry.getValue().get(0).getValue(), is(300L)); //300 is ~5min is the diff between two epoch times
                assertThat(entry.getValue().get(0).getLinkedBucketName(),is("21+km"));
            }
        }
    }

    @Test
    public void testPercentiles1km95PercentAndGroupByDaysAndHours() {

        /* Preparation */
        classUnderTest = new DurationPercentileService(95, GROUP_BY_MODE.DAYS_AND_HOURS); //<-DAYS_AND_HOURS
        List<Ride> rides = prepare21PluskmRidesWith1to5MinutesDuration();
        BucketRange<KmBucket> bucketRange = createBucketRange();
        
        /* Execution */
        for(Ride ride : rides) {
            classUnderTest.updatePercentiles(ride, bucketRange);
        }

        /* Validation */
        Map<String, List<Percentile>> percentilesPerHours = classUnderTest.getPercentilesGroupByTimeslot();
        assertNotNull(percentilesPerHours);
        assertThat(percentilesPerHours.size(), is(4));
        assertThat(percentilesPerHours, IsMapContaining.hasKey("2014-07-17 13:00"));
        assertThat(percentilesPerHours, IsMapContaining.hasKey("2014-07-17 14:00"));
        assertThat(percentilesPerHours, IsMapContaining.hasKey("2014-07-18 13:00"));
        assertThat(percentilesPerHours, IsMapContaining.hasKey("2014-07-18 14:00"));

        for(Map.Entry<String, List<Percentile>> entry: percentilesPerHours.entrySet()) {
            if(entry.getKey().equals("2014-07-17 13:00")) {
                assertThat(entry.getValue().size(), is(1));
                assertThat(entry.getValue().get(0).getValue(), is(300L));
            }
            if(entry.getKey().equals("2014-07-17 14:00")) {
                assertThat(entry.getValue().size(), is(1));
                assertThat(entry.getValue().get(0).getValue(), is(300L));
            }
            if(entry.getKey().equals("2014-07-18 13:00")) {
                assertThat(entry.getValue().size(), is(1));
                assertThat(entry.getValue().get(0).getValue(), is(300L));
            }
            if(entry.getKey().equals("2014-07-18 14:00")) {
                assertThat(entry.getValue().size(), is(1));
                assertThat(entry.getValue().get(0).getValue(), is(300L));
            }
        }
    }

    private List<Ride> prepare21PluskmRidesWith1to5MinutesDuration() {

        List<Ride> rides = new ArrayList<>();

        /* 1 Minute Durations */
        RideBuilder rideBuilder = new RideBuilder();
        rideBuilder.createNewRide("1");
        rideBuilder.addRoutePoint("37.921373", "23.929563", "1405591942"); //Thursday, July 17, 2014 1:12:22 PM
        rideBuilder.addRoutePoint("37.940637", "23.646486", "1405592024"); //Distance is 0.1910km and duration is 1m37s
        rides.add(rideBuilder.getRide());

        rideBuilder.createNewRide("2");
        rideBuilder.addRoutePoint("37.921373", "23.929563", "1405678342"); //Friday, July 18, 2014 1:12:22 PM
        rideBuilder.addRoutePoint("37.940637", "23.646486", "1405678424"); //Distance is 0.1910km and duration is 1m37s
        rides.add(rideBuilder.getRide());

        rideBuilder.createNewRide("3");
        rideBuilder.addRoutePoint("37.921373", "23.929563", "1405595542"); //Thursday, July 17, 2014 2:12:22 PM
        rideBuilder.addRoutePoint("37.940637", "23.646486", "1405595602"); //Distance is 0.1910km and duration is 1m0s
        rides.add(rideBuilder.getRide());

        rideBuilder.createNewRide("4");
        rideBuilder.addRoutePoint("37.921373", "23.929563", "1405681942"); //Thursday, July 18, 2014 2:12:22 PM
        rideBuilder.addRoutePoint("37.940637", "23.646486", "1405682002"); //Distance is 0.1910km and duration is 1m0s
        rides.add(rideBuilder.getRide());

        /* 2 Minutes Durations */
        rideBuilder.createNewRide("5");
        rideBuilder.addRoutePoint("37.921373", "23.929563", "1405591942"); //Thursday, July 17, 2014 1:12:22 PM
        rideBuilder.addRoutePoint("37.940637", "23.646486", "1405592062"); //Distance is 0.1910km and duration is 2m0s
        rides.add(rideBuilder.getRide());

        rideBuilder.createNewRide("6");
        rideBuilder.addRoutePoint("37.921373", "23.929563", "1405678342"); //Thursday, July 18, 2014 1:12:22 PM
        rideBuilder.addRoutePoint("37.940637", "23.646486", "1405678462"); //Distance is 0.1910km and duration is 2m0s
        rides.add(rideBuilder.getRide());

        rideBuilder.createNewRide("7");
        rideBuilder.addRoutePoint("37.921373", "23.929563", "1405595542"); //Thursday, July 17, 2014 2:12:22 PM
        rideBuilder.addRoutePoint("37.940637", "23.646486", "1405595662"); //Distance is 0.1910km and duration is 2m0s
        rides.add(rideBuilder.getRide());

        rideBuilder.createNewRide("8");
        rideBuilder.addRoutePoint("37.921373", "23.929563", "1405681942"); //Thursday, July 18, 2014 2:12:22 PM
        rideBuilder.addRoutePoint("37.940637", "23.646486", "1405682062"); //Distance is 0.1910km and duration is 1m0s
        rides.add(rideBuilder.getRide());

        /* 3 Minutes Durations */
        rideBuilder.createNewRide("9");
        rideBuilder.addRoutePoint("37.921373", "23.929563", "1405591942"); //Thursday, July 17, 2014 1:12:22 PM
        rideBuilder.addRoutePoint("37.940637", "23.646486", "1405592122"); //Distance is 0.1910km and duration is 3m0s
        rides.add(rideBuilder.getRide());

        rideBuilder.createNewRide("10");
        rideBuilder.addRoutePoint("37.921373", "23.929563", "1405678342"); //Thursday, July 18, 2014 1:12:22 PM
        rideBuilder.addRoutePoint("37.940637", "23.646486", "1405678522"); //Distance is 0.1910km and duration is 3m0s
        rides.add(rideBuilder.getRide());

        rideBuilder.createNewRide("11");
        rideBuilder.addRoutePoint("37.921373", "23.929563", "1405595542"); //Thursday, July 17, 2014 2:12:22 PM
        rideBuilder.addRoutePoint("37.940637", "23.646486", "1405595722"); //Distance is 0.1910km and duration is 3m0s
        rides.add(rideBuilder.getRide());

        rideBuilder.createNewRide("12");
        rideBuilder.addRoutePoint("37.921373", "23.929563", "1405681942"); //Thursday, July 18, 2014 2:12:22 PM
        rideBuilder.addRoutePoint("37.940637", "23.646486", "1405682122"); //Distance is 0.1910km and duration is 3m0s
        rides.add(rideBuilder.getRide());

        /* 4 Minutes Durations */
        rideBuilder.createNewRide("13");
        rideBuilder.addRoutePoint("37.921373", "23.929563", "1405591942"); //Thursday, July 17, 2014 1:12:22 PM
        rideBuilder.addRoutePoint("37.940637", "23.646486", "1405592182"); //Distance is 0.1910km and duration is 4m0s
        rides.add(rideBuilder.getRide());

        rideBuilder.createNewRide("14");
        rideBuilder.addRoutePoint("37.921373", "23.929563", "1405678342"); //Thursday, July 18, 2014 1:12:22 PM
        rideBuilder.addRoutePoint("37.940637", "23.646486", "1405678582"); //Distance is 0.1910km and duration is 4m0s
        rides.add(rideBuilder.getRide());

        rideBuilder.createNewRide("15");
        rideBuilder.addRoutePoint("37.921373", "23.929563", "1405595542"); //Thursday, July 17, 2014 2:12:22 PM
        rideBuilder.addRoutePoint("37.940637", "23.646486", "1405595782"); //Distance is 0.1910km and duration is 3m0s
        rides.add(rideBuilder.getRide());

        rideBuilder.createNewRide("16");
        rideBuilder.addRoutePoint("37.921373", "23.929563", "1405681942"); //Thursday, July 18, 2014 2:12:22 PM
        rideBuilder.addRoutePoint("37.940637", "23.646486", "1405682182"); //Distance is 0.1910km and duration is 3m0s
        rides.add(rideBuilder.getRide());

        /* 5 Minutes Durations */
        rideBuilder.createNewRide("17");
        rideBuilder.addRoutePoint("37.921373", "23.929563", "1405591942"); //Thursday, July 17, 2014 1:12:22 PM
        rideBuilder.addRoutePoint("37.940637", "23.646486", "1405592242"); //Distance is 0.1910km and duration is 5m0s
        rides.add(rideBuilder.getRide());

        rideBuilder.createNewRide("18");
        rideBuilder.addRoutePoint("37.921373", "23.929563", "1405678342"); //Thursday, July 18, 2014 1:12:22 PM
        rideBuilder.addRoutePoint("37.940637", "23.646486", "1405678642"); //Distance is 0.1910km and duration is 4m0s
        rides.add(rideBuilder.getRide());

        rideBuilder.createNewRide("19");
        rideBuilder.addRoutePoint("37.921373", "23.929563", "1405595542"); //Thursday, July 17, 2014 2:12:22 PM
        rideBuilder.addRoutePoint("37.940637", "23.646486", "1405595842"); //Distance is 0.1910km and duration is 3m0s
        rides.add(rideBuilder.getRide());

        rideBuilder.createNewRide("20");
        rideBuilder.addRoutePoint("37.921373", "23.929563", "1405681942"); //Thursday, July 18, 2014 2:12:22 PM
        rideBuilder.addRoutePoint("37.940637", "23.646486", "1405682242"); //Distance is 0.1910km and duration is 3m0s
        rides.add(rideBuilder.getRide());

        return rides;
    }

    private BucketRange<KmBucket> createBucketRange() {
        BucketRange<KmBucket> bucketRange = new BucketRange<KmBucket>();
        bucketRange.addBucket(new KmBucket("1km", 0, 1, 0));
        bucketRange.addBucket(new KmBucket("2km", 1, 2, 1));
        bucketRange.addBucket(new KmBucket("3km", 2, 3, 2));
        bucketRange.addBucket(new KmBucket("5km", 3, 5, 3));
        bucketRange.addBucket(new KmBucket("8km", 5, 8, 4));
        bucketRange.addBucket(new KmBucket("13km", 8, 13, 5));
        bucketRange.addBucket(new KmBucket("21km", 13, 21, 6));
        bucketRange.addBucket(new KmBucket("21+km", 21, null, 7));
        return bucketRange;
    }
}