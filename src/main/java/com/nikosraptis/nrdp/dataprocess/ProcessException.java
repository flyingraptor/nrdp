package com.nikosraptis.nrdp.dataprocess;

public class ProcessException extends RuntimeException{

    private static final long serialVersionUID = 1L;
    
    public ProcessException(String message) {
        super(message);
    }
}