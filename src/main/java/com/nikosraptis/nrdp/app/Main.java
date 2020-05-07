package com.nikosraptis.nrdp.app;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.nikosraptis.nrdp.csvread.RideDataCsvReader;
import com.nikosraptis.nrdp.csvwrite.PercentilesCsvWriter;
import com.nikosraptis.nrdp.dataprocess.DurationPercentilesWriter;
import com.nikosraptis.nrdp.dataprocess.PercentileService;
import com.nikosraptis.nrdp.dataprocess.ProcessService;
import com.nikosraptis.nrdp.dataprocess.percentile.DurationPercentileService;
import com.nikosraptis.nrdp.dataprocess.percentile.DurationPercentileService.GROUP_BY_MODE;
import com.nikosraptis.nrdp.dataprocess.ride.Ride;
import com.nikosraptis.nrdp.dataprocess.ride.RideDataReader;

/**
 * Main class has only initilization and wiring of the three main parts of the
 * program The Reader, the Process Service and the Writer. Also, it holds the
 * only Queue object for the communicaiton between the Reader and the Process
 * Service. Finally it does some validation in user input parameters.
 */
public final class Main {

    private static String inputFile;

    private static String outputFile = "./out.csv";

    private static int percentile = 95; /* The default target percentile */

    private static String groupByMode = "DAYS_AND_HOURS"; /* The default group by way for the results */

    /**
     * The program entry method. Initilizes the concrete implementations of the 3
     * major components and does the wiring between them. The Reader and the main
     * Process talk with a Queue in between and the Writer acts as an Observer for
     * any ready calculation to write in the out file.
     * 
     * @param args No argunements are used from the array. Instead all paramaters
     *             are passed with -Dname=value
     * @throws InterruptedException
     * @throws IOException
     */
    public static void main(String[] args) throws InterruptedException, IOException {

        extactAnyUserInputParameters();

        /* The Queue that holds only rides that are completely read from the file */
        BlockingQueue<Ride> ridesQueue = new LinkedBlockingQueue<Ride>();

        /* Initialize a Data Reader and give him the common queue */
        RideDataReader rideDataReader = new RideDataCsvReader(inputFile, ridesQueue);

        /*
         * Initialize the main process service with also a percentile service for
         * calculations and the common queue
         */
        PercentileService percentileService = new DurationPercentileService(percentile,
                GROUP_BY_MODE.valueOf(groupByMode));
        ProcessService processService = new ProcessService(ridesQueue, percentileService);

        /*
         * Initialize a Data Writer to write to the output destination. Also register it
         * to the process service to watch for ready calculations
         */
        DurationPercentilesWriter percentilesWriter = new PercentilesCsvWriter(outputFile);
        processService.registerObserver(percentilesWriter);

        /*
         * Start the producer worker thread which is the data reader and the consumer
         * worker which the processor of the data
         */
        Thread producer = new Thread(rideDataReader);
        Thread consumer = new Thread(processService);
        producer.start();
        consumer.start();
    }

    /**
     * VAlidate user input and also overiddes the default values in case custom
     * parameters are passed to the program from the user.
     */
    protected static void extactAnyUserInputParameters() {

        inputFile = System.getProperty("inputFile");
        if (inputFile == null) {
            System.out.println("Error: No file given");
            System.exit(1);
        }

        if (System.getProperty("outputFile") != null) {
            outputFile = System.getProperty("outputFile");
        }

        if (System.getProperty("percentile") != null) {
            percentile = Integer.parseInt(System.getProperty("percentile"));
        }

        if (System.getProperty("groupByMode") != null) {
            groupByMode = System.getProperty("groupByMode");
        }
    }
}
