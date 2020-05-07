package com.nikosraptis.nrdp.dataprocess;

import java.util.List;

import com.nikosraptis.nrdp.dataprocess.percentile.Percentile;

public interface ProcessObserver {
    public void update(String dayAndTime, List<Percentile> record);
}