package com.cabservice.cab_service.event;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class BookingCompletedEvent implements DomainEvent {
    public static final String EVENT_NAME = "BOOKING_COMPLETED";
    private Long bookingId;
    private Long cabId;
    private Long sourceCityId;
    private Long destinationCityId;
    private Instant timestamp;
    @Override
    public String getEventName() {
        return EVENT_NAME;
    }
}
