package com.cabservice.cab_service.enums;

public enum BookingState {
    SCHEDULED,  // Booking created but trip not started
    ONGOING,    // Trip has started
    COMPLETED,  // Trip finished
    CANCELLED   // Booking cancelled
}
