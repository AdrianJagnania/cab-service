package com.cabservice.cab_service.event;

public class NoCabAvailableException extends RuntimeException {
    public NoCabAvailableException(String message) {
        super(message);
    }
}

