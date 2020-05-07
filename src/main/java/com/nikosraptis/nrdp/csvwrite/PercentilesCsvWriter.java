package com.nikosraptis.nrdp.csvwrite;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import com.nikosraptis.nrdp.dataprocess.DurationPercentilesWriter;
import com.nikosraptis.nrdp.csvwrite.PercentileValueFormatter.Format;
import com.nikosraptis.nrdp.dataprocess.percentile.Percentile;

/**
 * Responsible for writing the percentiles for each bucket and group by approach
 * to an out csv file. Since it DurationPercentilesWriter interface which also
 * extends the ProcessObserver interface, the class acts as an observer to the
 * process service for ready calculation to write them in the out csv.
 */
public class PercentilesCsvWriter implements DurationPercentilesWriter {

    private String filename;

    /**
     * Constructor
     * 
     * @param filename The file to write the results.
     */
    public PercentilesCsvWriter(String filename) throws IOException {

        this.filename = filename;
        File file = new File(filename);
        if (!file.exists()) {
            file.createNewFile();
        }
        this.filename = filename;
    }

    /**
     * The method it is forced by the ProcessObserver interface and it is the
     * trigger point for this class to write any updates
     * 
     * @param timeslot    The timeslot is the group by approach used to group each
     *                    percentiles
     * @param percentiles The corresponding percentiles for this timeslot
     */
    @Override
    public void update(String timeslot, List<Percentile> percentiles) {
        this.write(timeslot, percentiles);
    }

    /**
     * The write method is also enforced by the interface that any percentiles
     * writer should implement. It sorts the percentiles and formats them to the
     * desired structure in the file. Finally it calls a method to replace the
     * existing file.
     * 
     * @param timeslot    The timeslot is the group by approach used to group each
     *                    percentile
     * @param percentiles The corresponding percentiles for this timeslot
     */
    @Override
    public void write(String timeslot, List<Percentile> percentiles) {

        PercentileValueFormatter percentileValueFormatter = new PercentileValueFormatter();
        Collections.sort(percentiles);

        /* Prepare the record for the out file */
        String formattedValue = timeslot + ",";
        for (int i = 0; i < percentiles.size(); i++) {
            formattedValue += percentileValueFormatter.format(Format.DEFAULT, percentiles.get(i).getValue());

            /* Don't add comma if it's the last percentile */
            if (i + 1 != percentiles.size()) {
                formattedValue += ",";
            }
        }
        replaceFileWithTheNewPercentiles(timeslot, formattedValue, filename);
    }

    /**
     * Overrides the current (if any) out file with the existing values and an
     * updated one. If it is new it just adds it.
     * 
     * @param timeslot The timeslot is the group by approach used to group each
     *                 percentile
     * @param newValue The file record ready to be added to the file with the proper
     *                 format.
     * @param filename The outpout file filename
     */
    protected void replaceFileWithTheNewPercentiles(String timeslot, String newValue, String filename) {
        try {
            BufferedReader file = new BufferedReader(new FileReader(filename));
            StringBuffer inputBuffer = new StringBuffer();
            String line;

            boolean valueExists = false;

            while ((line = file.readLine()) != null) {
                if (line.startsWith(timeslot)) {
                    valueExists = true;
                    line = newValue;
                }
                inputBuffer.append(line);
                inputBuffer.append(System.lineSeparator());
            }

            if (!valueExists) {
                inputBuffer.append(newValue);
            }

            file.close();

            /* write the new string with the replaced line OVER the same file */
            FileOutputStream fileOut = new FileOutputStream(filename);
            fileOut.write(inputBuffer.toString().getBytes());
            fileOut.close();

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

}