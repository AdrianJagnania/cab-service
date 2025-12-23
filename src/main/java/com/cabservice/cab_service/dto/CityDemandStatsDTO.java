package com.cabservice.cab_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class CityDemandStatsDTO {
    private Long cityId;
    private long totalBookings;
    private Instant busiestHourStart;
    private long busiestHourBookings;
}

