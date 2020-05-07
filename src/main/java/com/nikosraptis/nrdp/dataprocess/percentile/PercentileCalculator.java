package com.nikosraptis.nrdp.dataprocess.percentile;

import java.util.Collections;
import java.util.List;

/**
 * Responsible for calculating a percentile
 */
class PercentileCalculator {

    /**
     * The method that calculates a percentile give a list of values and the
     * requested percentile.
     * 
     * @param values     A list of values
     * @param percentile The requested percentile. E.g 95%
     * @return
     */
    public long percentileOf(List<Long> values, double percentile) {

        if (values == null) {
            throw new PercentileException("Error: Can't calculate percentiles with null");
        }

        if (values.size() == 0) {
            return 0L;
        }

        if (percentile <= 0 || percentile > 100) {
            throw new PercentileException("Error: Can't calculate percentiles with " + percentile + "%");
        }

        Collections.sort(values);
        /* The approach is to Rund a number upward to its nearest integer: */
        int index = (int) Math.ceil(((double) percentile / (double) 100) * (double) values.size());
        return values.get(index - 1);
    }
}