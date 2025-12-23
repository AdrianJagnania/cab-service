package com.cabservice.cab_service.service;

import com.cabservice.cab_service.entity.Booking;

public interface BookingService {
    Booking bookCab(Long sourceCityId, Long destinationCityId);
    Booking completeTrip(Long bookingId);
    Booking startTrip (Long bookingId);
}
