package com.cabservice.cab_service.service.impl;

import com.cabservice.cab_service.entity.Booking;
import com.cabservice.cab_service.entity.Cab;
import com.cabservice.cab_service.enums.BookingState;
import com.cabservice.cab_service.enums.CabState;
import com.cabservice.cab_service.repository.BookingRepository;
import com.cabservice.cab_service.repository.CabRepository;
import com.cabservice.cab_service.service.BookingService;
import com.cabservice.cab_service.strategy.CabAssignmentStrategy;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.logging.Logger;

@Service
public class BookingServiceImpl implements BookingService{
    private static final Logger logger = Logger.getLogger(String.valueOf(BookingServiceImpl.class));
    private final CabRepository cabRepository;
    private final CabAssignmentStrategy strategy;
    private final BookingRepository bookingRepository;

    public BookingServiceImpl(CabRepository cabRepository, CabAssignmentStrategy strategy, BookingRepository bookingRepository) {
        this.cabRepository = cabRepository;
        this.strategy = strategy;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public Booking bookCab(Long sourceCityId, Long destinationCityId) {
        logger.info("cabRepository.findAll(): " + cabRepository.findAll());
        List<Cab> eligibleCabs = cabRepository.findByStateAndCityId(CabState.IDLE,sourceCityId);

        logger.info("eligible cabs: "+ eligibleCabs);
        Cab assignedCab = strategy.assignCab(eligibleCabs);

        Instant now = Instant.now();
        assignedCab.setState(CabState.ON_TRIP);
        assignedCab.setCity(null);
        assignedCab.setLastStateChangeTime(now);
        cabRepository.save(assignedCab);


        Booking booking = new Booking(assignedCab.getCabId(), sourceCityId, destinationCityId);
        booking.setState(BookingState.ONGOING);
        bookingRepository.save(booking);
        return booking;
    }

    @Override
    public Booking completeTrip(Long bookingId){
        Booking booking = bookingRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        Cab cab = cabRepository.findByCabId(booking.getCabId())
                .orElseThrow(() -> new IllegalArgumentException("Cab not found"));

        Instant now = Instant.now();
        cab.setState(CabState.IDLE);
        cab.setLastStateChangeTime(now);
        cabRepository.save(cab);

        booking.setState(BookingState.COMPLETED);
        bookingRepository.save(booking);
        return booking;
    }

}
