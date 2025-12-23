package com.cabservice.cab_service.controller;

import com.cabservice.cab_service.dto.CreateBookingRequestDTO;
import com.cabservice.cab_service.entity.Booking;
import com.cabservice.cab_service.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<String> bookCab(
            @RequestBody CreateBookingRequestDTO request) {

        bookingService.bookCab(request.getSourceCityId(), request.getDestinationCityId());
        return ResponseEntity.ok("Cab booked successfully");
    }

    @PostMapping("/{bookingId}/complete")
    public ResponseEntity<String> completeTrip(@RequestBody Booking booking) {
        bookingService.completeTrip(booking);
        return ResponseEntity.ok("Trip completed and cab is now IDLE");
    }
}
