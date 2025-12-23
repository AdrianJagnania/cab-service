package com.cabservice.cab_service;

import com.cabservice.cab_service.event.DomainEventPublisher;
import com.cabservice.cab_service.event.NoCabAvailableException;
import com.cabservice.cab_service.entity.Booking;
import com.cabservice.cab_service.entity.Cab;
import com.cabservice.cab_service.enums.BookingState;
import com.cabservice.cab_service.enums.CabState;
import com.cabservice.cab_service.repository.BookingRepository;
import com.cabservice.cab_service.repository.CabRepository;
import com.cabservice.cab_service.service.impl.BookingServiceImpl;
import com.cabservice.cab_service.strategy.CabAssignmentStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private CabRepository cabRepository;

    @Mock
    private CabAssignmentStrategy cabAssignmentStrategy;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private DomainEventPublisher eventPublisher;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private Cab idleCab;

    @BeforeEach
    void setUp() {
        idleCab = new Cab();
        idleCab.setCabId(1L);
        idleCab.setState(CabState.IDLE);
        idleCab.setCityId(10L);
    }

    @Test
    void bookCab_assignsCabAndCreatesBooking() {
        Long sourceCityId = 10L;
        Long destinationCityId = 20L;

        when(cabRepository.findByStateAndCityId(CabState.IDLE, sourceCityId))
                .thenReturn(List.of(idleCab));
        when(cabAssignmentStrategy.assignCab(List.of(idleCab))).thenReturn(idleCab);

        ArgumentCaptor<Booking> bookingCaptor = ArgumentCaptor.forClass(Booking.class);

        Booking booking = bookingService.bookCab(sourceCityId, destinationCityId);

        // Verify booking saved with expected fields
        verify(bookingRepository).save(bookingCaptor.capture());
        Booking saved = bookingCaptor.getValue();
        assertThat(saved.getCabId()).isEqualTo(idleCab.getCabId());
        assertThat(saved.getSourceCityId()).isEqualTo(sourceCityId);
        assertThat(saved.getDestinationCityId()).isEqualTo(destinationCityId);
        assertThat(saved.getState()).isEqualTo(BookingState.ONGOING);

        // Returned booking should match saved one in key aspects
        assertThat(booking.getCabId()).isEqualTo(idleCab.getCabId());
        assertThat(booking.getState()).isEqualTo(BookingState.ONGOING);

        // Cab should be marked ON_TRIP and saved
        verify(cabRepository).save(idleCab);
        assertThat(idleCab.getState()).isEqualTo(CabState.ON_TRIP);
        verify(eventPublisher, atLeastOnce()).publish(any());
    }

    @Test
    void bookCab_throwsWhenNoEligibleCabs() {
        Long sourceCityId = 10L;

        when(cabRepository.findByStateAndCityId(CabState.IDLE, sourceCityId))
                .thenReturn(List.of());

        assertThrows(NoCabAvailableException.class,
                () -> bookingService.bookCab(sourceCityId, 20L));
    }

    @Test
    void completeTrip_marksBookingCompletedAndCabIdle() {
        Long bookingId = 100L;
        Long cabId = 1L;

        Booking booking = Booking.createOngoing(cabId, 10L, 20L);
        booking.setBookingId(bookingId);

        Cab cab = new Cab();
        cab.setCabId(cabId);
        cab.setState(CabState.ON_TRIP);
        cab.setCityId(10L);

        when(bookingRepository.findByBookingId(bookingId)).thenReturn(Optional.of(booking));
        when(cabRepository.findByCabId(cabId)).thenReturn(Optional.of(cab));

        ArgumentCaptor<Cab> cabCaptor = ArgumentCaptor.forClass(Cab.class);
        ArgumentCaptor<Booking> bookingCaptor = ArgumentCaptor.forClass(Booking.class);

        Booking result = bookingService.completeTrip(bookingId);

        // Cab is saved back as IDLE
        verify(cabRepository).save(cabCaptor.capture());
        Cab savedCab = cabCaptor.getValue();
        assertThat(savedCab.getState()).isEqualTo(CabState.IDLE);

        // Booking is saved as COMPLETED
        verify(bookingRepository).save(bookingCaptor.capture());
        Booking savedBooking = bookingCaptor.getValue();
        assertThat(savedBooking.getState()).isEqualTo(BookingState.COMPLETED);

        // Returned booking reflects completion
        assertThat(result.getState()).isEqualTo(BookingState.COMPLETED);

        // Events published for cab state change and booking completion
        verify(eventPublisher, atLeastOnce()).publish(any());
    }
}
