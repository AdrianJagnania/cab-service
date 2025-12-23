package com.cabservice.cab_service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class BookingDemandEvent {
    private Long cityId;
    private Instant timestamp;
}

