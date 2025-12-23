package com.cabservice.cab_service.entity;

import com.cabservice.cab_service.enums.BookingState;
import lombok.Data;

@Data
public class Booking {
    private Long bookingId;
    private Long cabId;
    private Long sourceCityId;
    private Long destinationCityId;
    private BookingState state;

    public Booking(Long cabId, Long sourceCityId, Long destinationCityId) {
        this.cabId = cabId;
        this.sourceCityId = sourceCityId;
        this.destinationCityId = destinationCityId;
        this.state = BookingState.SCHEDULED;
    }
}
