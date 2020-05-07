package com.nikosraptis.nrdp.dataprocess;

import java.util.List;

import com.nikosraptis.nrdp.dataprocess.percentile.Percentile;

/** Should be implemented from any Percentiles Writer */
public interface DurationPercentilesWriter extends ProcessObserver {
    public void write(String dayAndTime, List<Percentile> percentiles);
}