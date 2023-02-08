package com.dashboard.Exceptions;

public class ScannerAggregationException extends Exception {
    public ScannerAggregationException(String errorMessage, Exception e){
        super(errorMessage, e);
    }
}
