package com.nikosraptis.nrdp.dataprocess;

import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.nikosraptis.nrdp.dataprocess.ProcessObserver;
import com.nikosraptis.nrdp.dataprocess.percentile.DurationPercentileService;
import com.nikosraptis.nrdp.dataprocess.percentile.DurationPercentileService.GROUP_BY_MODE;
import com.nikosraptis.nrdp.dataprocess.percentile.Percentile;
import com.nikosraptis.nrdp.dataprocess.ride.Ride;
import com.nikosraptis.nrdp.dataprocess.ride.RideBuilder;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class ProcessServiceUnitTest {

    private ProcessService classUnderTest;

    private PercentileService percentileService;

    private static boolean reachedTheObserverFlag = false;

    @Test
    public void testReadFromQueueAndInformTheRegisteredObserver() {

        /* Praparation */
        final BlockingQueue<Ride> ridesQueue = prepareTheQueueWith1Ride();
        percentileService = new DurationPercentileService(95, GROUP_BY_MODE.valueOf("HOURS"));
        classUnderTest = new ProcessService(ridesQueue, percentileService);
        classUnderTest.registerObserver(new CustomObserverStub());

        /* Execution */
        classUnderTest.run();

        /* Validation */
        assertThat(reachedTheObserverFlag, is(true));
    }

    private BlockingQueue<Ride> prepareTheQueueWith1Ride() {
        final BlockingQueue<Ride> ridesQueue = new LinkedBlockingQueue<Ride>();

        final RideBuilder rideBuilder = new RideBuilder();
        rideBuilder.createNewRide("1");
        rideBuilder.addRoutePoint("37.921373", "23.929563", "1405591942");
        rideBuilder.addRoutePoint("37.940637", "23.646486", "1405592024");
        ridesQueue.add(rideBuilder.getRide());

        return ridesQueue;
    }

    public static void setReachedTheObserverFlag(boolean flag) {
        reachedTheObserverFlag = flag;
    }
}

class CustomObserverStub implements ProcessObserver {

    @Override
    public void update(String dayAndTime, List<Percentile> record) {
        assertNotNull(dayAndTime);
        assertNotNull(record);
        ProcessServiceUnitTest.setReachedTheObserverFlag(true);
    }
}