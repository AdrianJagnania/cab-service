package com.cabservice.cab_service.service.impl;

import com.cabservice.cab_service.event.BookingCompletedEvent;
import com.cabservice.cab_service.event.BookingCreatedEvent;
import com.cabservice.cab_service.event.CabStateChangedEvent;
import com.cabservice.cab_service.event.DomainEventPublisher;
import com.cabservice.cab_service.event.NoCabAvailableException;
import com.cabservice.cab_service.entity.Booking;
import com.cabservice.cab_service.entity.Cab;
import com.cabservice.cab_service.enums.BookingState;
import com.cabservice.cab_service.enums.CabState;
import com.cabservice.cab_service.repository.BookingRepository;
import com.cabservice.cab_service.repository.CabRepository;
import com.cabservice.cab_service.service.BookingService;
import com.cabservice.cab_service.service.CabService;
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
    private final DomainEventPublisher eventPublisher;
    private final CabService cabService;

    public BookingServiceImpl(CabRepository cabRepository,
                              CabAssignmentStrategy strategy,
                              BookingRepository bookingRepository,
                              DomainEventPublisher eventPublisher, CabService cabService) {
        this.cabRepository = cabRepository;
        this.strategy = strategy;
        this.bookingRepository = bookingRepository;
        this.eventPublisher = eventPublisher;
        this.cabService = cabService;
    }

    @Override
    public Booking bookCab(Long sourceCityId, Long destinationCityId) {
        logger.info("cabRepository.findAll(): " + cabRepository.findAll());
        List<Cab> eligibleCabs = cabRepository.findByStateAndCityId(CabState.IDLE,sourceCityId);

        logger.info("eligible cabs: "+ eligibleCabs);
        if (eligibleCabs == null || eligibleCabs.isEmpty()) {
            throw new NoCabAvailableException("No cab available");
        }
        Cab assignedCab = strategy.assignCab(eligibleCabs);

        Instant now = Instant.now();
        assignedCab.setState(CabState.ON_TRIP);
        assignedCab.setCityId(null);
        assignedCab.setLastStateChangeTime(now);
        cabRepository.save(assignedCab);

        eventPublisher.publish(new CabStateChangedEvent(
                assignedCab.getCabId(),
                CabState.IDLE,
                CabState.ON_TRIP,
                null,
                now));

        Booking booking = Booking.createOngoing(assignedCab.getCabId(), sourceCityId, destinationCityId);
        bookingRepository.save(booking);

        eventPublisher.publish(new BookingCreatedEvent(
                booking.getBookingId(),
                booking.getCabId(),
                booking.getSourceCityId(),
                booking.getDestinationCityId(),
                now));
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

        eventPublisher.publish(new CabStateChangedEvent(
                cab.getCabId(),
                CabState.ON_TRIP,
                CabState.IDLE,
                cab.getCityId(),
                now));

        booking.setState(BookingState.COMPLETED);
        bookingRepository.save(booking);

        eventPublisher.publish(new BookingCompletedEvent(
                booking.getBookingId(),
                booking.getCabId(),
                booking.getSourceCityId(),
                booking.getDestinationCityId(),
                now));

        return booking;
    }

    @Override
    public Booking startTrip(Long bookingId){
        Booking booking = bookingRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        cabService.updateState(booking.getCabId(), CabState.ON_TRIP);
        booking.setState(BookingState.ONGOING);
        bookingRepository.save(booking);
        return booking;
    }


}
