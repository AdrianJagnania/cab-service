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

    private Booking(Long cabId, Long sourceCityId, Long destinationCityId, BookingState state) {
        this.cabId = cabId;
        this.sourceCityId = sourceCityId;
        this.destinationCityId = destinationCityId;
        this.state = state;
    }

    public static Booking createScheduled(Long cabId, Long sourceCityId, Long destinationCityId) {
        return new Booking(cabId, sourceCityId, destinationCityId, BookingState.SCHEDULED);
    }

    public static Booking createOngoing(Long cabId, Long sourceCityId, Long destinationCityId) {
        return new Booking(cabId, sourceCityId, destinationCityId, BookingState.ONGOING);
    }
}
