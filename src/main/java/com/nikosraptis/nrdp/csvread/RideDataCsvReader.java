package com.nikosraptis.nrdp.csvread;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

import com.nikosraptis.nrdp.dataprocess.ride.Ride;
import com.nikosraptis.nrdp.dataprocess.ride.RideBuilder;
import com.nikosraptis.nrdp.dataprocess.ride.RideDataReader;

/**
 * Responsible for reading the ride data from a csv file with format ...
 * rideId,latitude,longitude,unixTimestamp
 * rideId,latitude,longitude,unixTimestamp ... and send every completed read
 * ride to a Queue.
 */
public class RideDataCsvReader implements RideDataReader {

    private BlockingQueue<Ride> ridesQueue;

    private static final int RIDE_ID = 0;

    private static final int LATIDUTE = 1;

    private static final int LONGITUDE = 2;

    private static final int UNIX_TIMESTAMP = 3;

    private String pathToInputCsv;

    private FileInputStream inputStream = null;

    private String cvsSplitBy = ",";

    Map<String, Ride> rideEntries = new HashMap<>();

    /**
     * @param pathToInputCsv The path to the input csv file
     * @param ridesQueue     The queue to send the rides
     */
    public RideDataCsvReader(String pathToInputCsv, BlockingQueue<Ride> ridesQueue) {
        this.pathToInputCsv = pathToInputCsv;
        this.ridesQueue = ridesQueue;
    }

    /**
     * The thread entry point. Just initializes the stream and catch severe
     * exceptions. The actual wors is passed to read.
     */
    @Override
    public void run() {
        try {
            FileInputStream inputStream = new FileInputStream(pathToInputCsv);
            read(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        }
    }

    /**
     * The read does the actual read from the file line by line. When it see that
     * all entries of a ride are read, it sends the ride to the queue, clean the
     * local memory and continue to the next ride. In that way it avoid out of
     * memory exception with assumption that the route points of a ride are not a
     * huge number.
     * 
     * @param inputStream The stream that will be used to read the data.
     */
    protected void read(InputStream inputStream) {

        Scanner fileScanner = new Scanner(inputStream, "UTF-8");
        int counter = 0;
        String rideId = "";

        RideBuilder rideBuilder = new RideBuilder();
        String line = "";
        while (fileScanner.hasNextLine()) {

            counter++;
            line = fileScanner.nextLine();
            String[] currentEntry = line.split(cvsSplitBy);

            /*
             * Check if it's the first entry in the file in order to keep the rideId, so
             * later we know if we are done with that ride or not.
             */
            if (counter == 1) {
                rideId = currentEntry[RIDE_ID];
            }

            if (!rideId.equals(currentEntry[RIDE_ID]) || !fileScanner.hasNextLine()) {
                /*
                 * It means that we are done with all related entries of a ride in file and now
                 * we put it to the Queue and remove it from local memory
                 */
                ridesQueue.add(rideBuilder.getRide());
                rideEntries.remove(rideId);
                rideId = currentEntry[RIDE_ID];
            } else {
                processEntrysAndUpdadeRide(rideBuilder, rideEntries, currentEntry);
            }
        }

        if (fileScanner != null) {
            fileScanner.close();
        }
    }

    /**
     * This method parse it's column of the csv and either it creates a new Ride if
     * it is the first related entry or update an existing ride record in the local
     * memory.
     * 
     * @param rideBuilder  A builder to east the ride creation and decouple the data
     *                     format expectations
     * @param rideEntries  The local memory structure for the rides.
     * @param currentEntry The current entry
     */
    protected void processEntrysAndUpdadeRide(RideBuilder rideBuilder, Map<String, Ride> rideEntries,
            String[] currentEntry) {
        if (rideEntries.get(currentEntry[RIDE_ID]) == null) {
            rideBuilder.createNewRide(currentEntry[RIDE_ID]);
            rideBuilder.addRoutePoint(currentEntry[LATIDUTE], currentEntry[LONGITUDE], currentEntry[UNIX_TIMESTAMP]);
            rideEntries.put(currentEntry[RIDE_ID], rideBuilder.getRide());
        } else {
            Ride ride = rideEntries.get(currentEntry[RIDE_ID]);
            rideBuilder = new RideBuilder(ride);
            rideBuilder.addRoutePoint(currentEntry[LATIDUTE], currentEntry[LONGITUDE], currentEntry[UNIX_TIMESTAMP]);
        }
    }
}