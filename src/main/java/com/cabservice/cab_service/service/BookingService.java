package com.cabservice.cab_service.service;

import com.cabservice.cab_service.entity.Booking;
import com.cabservice.cab_service.entity.Cab;

public interface BookingService {
    Booking bookCab(Long sourceCityId, Long destinationCity);
    void completeTrip(Booking booking);
}
