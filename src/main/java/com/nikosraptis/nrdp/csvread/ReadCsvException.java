package com.nikosraptis.nrdp.csvread;

import com.nikosraptis.nrdp.dataprocess.ProcessException;

public class ReadCsvException extends ProcessException {

    private static final long serialVersionUID = 45804303982308453L;

    public ReadCsvException(String message) {
        super(message);
    }
}