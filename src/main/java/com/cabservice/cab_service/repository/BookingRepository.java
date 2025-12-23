package com.cabservice.cab_service.repository;

import com.cabservice.cab_service.entity.Booking;

import java.util.List;
import java.util.Optional;

public interface BookingRepository {
    void save(Booking booking);
    Optional<Booking> findByBookingId(Long bookingId);
    List<Booking> findAll();
}
