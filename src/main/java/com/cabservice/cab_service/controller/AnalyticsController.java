package com.cabservice.cab_service.controller;

import com.cabservice.cab_service.dto.CabIdleTimeResponseDTO;
import com.cabservice.cab_service.dto.CityDemandStatsDTO;
import com.cabservice.cab_service.entity.CabStateHistoryEntry;
import com.cabservice.cab_service.service.AnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/cabs/{cabId}/idle-time")
    public ResponseEntity<CabIdleTimeResponseDTO> getCabIdleTime(
            @PathVariable Long cabId,
            @RequestParam("from") Instant from,
            @RequestParam("to") Instant to) {

        long idleSeconds = analyticsService.getCabIdleSeconds(cabId, from, to);
        CabIdleTimeResponseDTO response = new CabIdleTimeResponseDTO(cabId, from, to, idleSeconds);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cabs/{cabId}/history")
    public ResponseEntity<List<CabStateHistoryEntry>> getCabHistory(@PathVariable Long cabId) {
        List<CabStateHistoryEntry> history = analyticsService.getCabHistory(cabId);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/demand")
    public ResponseEntity<List<CityDemandStatsDTO>> getDemandStats(
            @RequestParam("from") Instant from,
            @RequestParam("to") Instant to) {

        List<CityDemandStatsDTO> stats = analyticsService.getCityDemandStats(from, to);
        return ResponseEntity.ok(stats);
    }
}

