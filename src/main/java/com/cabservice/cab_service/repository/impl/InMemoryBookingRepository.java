package com.cabservice.cab_service.repository.impl;

import com.cabservice.cab_service.entity.Booking;
import com.cabservice.cab_service.repository.BookingRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryBookingRepository implements BookingRepository {
    private final Map<Long, Booking> bookings = new ConcurrentHashMap<>();
    @Override
    public void save(Booking booking) {
        if(booking.getBookingId() == null) {
            Long newId = (long) bookings.size() + 1;
            booking.setBookingId(newId);
        }
        bookings.put(booking.getBookingId(),booking);
    }

    @Override
    public Optional<Booking> findByBookingId(Long bookingId) {
        return Optional.ofNullable(bookings.get(bookingId));
    }

    @Override
    public List<Booking> findAll() {
        return new ArrayList<>(bookings.values());
    }
}
