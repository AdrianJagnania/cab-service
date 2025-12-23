package com.cabservice.cab_service;

public class NoCabAvailableException extends RuntimeException {
    public NoCabAvailableException(String message) {
        super(message);
    }
}

