package com.cabservice.cab_service.service;

import com.cabservice.cab_service.dto.CityDemandStatsDTO;
import com.cabservice.cab_service.entity.CabStateHistoryEntry;
import com.cabservice.cab_service.enums.CabState;

import java.time.Instant;
import java.util.List;

public interface AnalyticsService {
    void recordCabStateChange(Long cabId, CabState newState, Long cityId, Instant timestamp);
    void recordBookingDemand(Long sourceCityId, Instant timestamp);
    long getCabIdleSeconds(Long cabId, Instant from, Instant to);
    List<CabStateHistoryEntry> getCabHistory(Long cabId);
    List<CityDemandStatsDTO> getCityDemandStats(Instant from, Instant to);
}

