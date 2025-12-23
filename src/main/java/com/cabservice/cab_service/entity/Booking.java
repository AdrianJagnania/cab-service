package com.cabservice.cab_service.entity;

import com.cabservice.cab_service.enums.BookingState;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
@Data
public class Booking {
    private Long bookingId;
    private Long cabId;
    private Long sourceCityId;
    private Long destinationCityId;

    @Enumerated(EnumType.STRING)
    private BookingState state; // SCHEDULED, ONGOING, COMPLETED

    public Booking(Long bookingId, Long sourceCityId, Long destinationCityId) {
    }
}
