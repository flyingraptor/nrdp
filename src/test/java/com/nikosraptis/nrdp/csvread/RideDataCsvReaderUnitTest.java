package com.nikosraptis.nrdp.csvread;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.nikosraptis.nrdp.dataprocess.ride.Ride;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class RideDataCsvReaderUnitTest {

    private RideDataCsvReader classUnderTest;

    @Test
    public void testReadAllRidePartsAndPutRideToQueue() throws IOException, InterruptedException {

        /* Preparation */
        final BlockingQueue<Ride> ridesQueue = new LinkedBlockingQueue<Ride>();
        classUnderTest = new RideDataCsvReader("dummy.csv", ridesQueue);

        /* Execution */
        InputStream inputStream = prepareInputData();
        classUnderTest.read(inputStream);

        /* Validation */
        Ride ride = ridesQueue.poll(2, TimeUnit.SECONDS); //Wait 2 seconds before poll
        assertNotNull(ride);
        assertThat(ride.getId(), is(1));

    }

    private InputStream prepareInputData() throws IOException {
        return IOUtils.toInputStream("1,37.966660,23.728308,1405594957\n" +
        "1,37.966627,23.728263,1405594966\n" +
        "1,37.966625,23.728263,1405594974\n" +
        "1,37.966613,23.728375,1405594984\n" +
        "1,37.966203,23.728597,1405594992\n" +
        "1,37.966195,23.728613,1405595001\n" +
        "1,37.966195,23.728613,1405595009\n" +
        "1,37.966195,23.728613,1405595017\n" +
        "1,37.966195,23.728613,1405595026", "UTF-8");
    }
}