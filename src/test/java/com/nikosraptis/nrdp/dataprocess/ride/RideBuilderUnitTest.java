package com.nikosraptis.nrdp.dataprocess.ride;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;

public class RideBuilderUnitTest {

    private RideBuilder classUnderTest;

    @Test
    public void testBuildARide() {

        /* Preparation */
        classUnderTest = new RideBuilder();

        /* Execution */
        classUnderTest.createNewRide("1");
        classUnderTest.addRoutePoint("38.006775", "23.798758", "1405589088");

        /* Validation */
        Ride ride = classUnderTest.getRide();
        assertNotNull(ride);
        assertThat(ride.getId(), is(1));
        assertThat(ride.getDistance(), is(0.0));
        assertThat(ride.getStartTimestamp(), is(1405589088L));
        assertThat(ride.getEndTimestamp(), is(1405589088L));
    }
    
}