package com.nikosraptis.nrdp.dataprocess;

public interface Bucket extends Comparable<Bucket> {
    public String getName();
    public Integer getLeftLimit();
    public Integer getRightLimit();
    public Integer getRank();
}