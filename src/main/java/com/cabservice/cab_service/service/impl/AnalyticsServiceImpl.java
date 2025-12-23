package com.cabservice.cab_service.service.impl;

import com.cabservice.cab_service.dto.CityDemandStatsDTO;
import com.cabservice.cab_service.entity.BookingDemandEvent;
import com.cabservice.cab_service.entity.CabStateHistoryEntry;
import com.cabservice.cab_service.enums.CabState;
import com.cabservice.cab_service.service.AnalyticsService;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    private final List<CabStateHistoryEntry> cabHistory = new ArrayList<>();
    private final List<BookingDemandEvent> bookingEvents = new ArrayList<>();

    @Override
    public synchronized void recordCabStateChange(Long cabId, CabState newState, Long cityId, Instant timestamp) {
        cabHistory.add(new CabStateHistoryEntry(cabId, newState, cityId, timestamp));
    }

    @Override
    public synchronized void recordBookingDemand(Long sourceCityId, Instant timestamp) {
        bookingEvents.add(new BookingDemandEvent(sourceCityId, timestamp));
    }

    @Override
    public long getCabIdleSeconds(Long cabId, Instant from, Instant to) {
        if (from == null || to == null || !from.isBefore(to)) {
            return 0L;
        }

        List<CabStateHistoryEntry> history = cabHistory.stream()
                .filter(e -> e.getCabId().equals(cabId))
                .sorted(Comparator.comparing(CabStateHistoryEntry::getTimestamp))
                .collect(Collectors.toList());

        if (history.isEmpty()) {
            return 0L;
        }

        Instant windowStart = from;
        Instant windowEnd = to;

        CabState currentState;

        CabStateHistoryEntry lastBefore = null;
        for (CabStateHistoryEntry entry : history) {
            if (!entry.getTimestamp().isAfter(windowStart)) {
                lastBefore = entry;
            } else {
                break;
            }
        }

        if (lastBefore != null) {
            currentState = lastBefore.getState();
        } else {
            CabStateHistoryEntry first = history.get(0);
            currentState = first.getState();
            if (first.getTimestamp().isAfter(windowStart)) {
                windowStart = first.getTimestamp();
                if (!windowStart.isBefore(windowEnd)) {
                    return 0L;
                }
            }
        }

        Instant cursor = windowStart;
        long idleSeconds = 0L;

        for (CabStateHistoryEntry entry : history) {
            Instant t = entry.getTimestamp();
            if (!t.isAfter(windowStart) || !t.isBefore(windowEnd)) {
                continue;
            }
            if (currentState == CabState.IDLE) {
                idleSeconds += Duration.between(cursor, t).getSeconds();
            }
            currentState = entry.getState();
            cursor = t;
        }

        if (currentState == CabState.IDLE && cursor.isBefore(windowEnd)) {
            idleSeconds += Duration.between(cursor, windowEnd).getSeconds();
        }

        return idleSeconds;
    }

    @Override
    public List<CabStateHistoryEntry> getCabHistory(Long cabId) {
        return cabHistory.stream()
                .filter(e -> e.getCabId().equals(cabId))
                .sorted(Comparator.comparing(CabStateHistoryEntry::getTimestamp))
                .collect(Collectors.toList());
    }

    @Override
    public List<CityDemandStatsDTO> getCityDemandStats(Instant from, Instant to) {
        if (from == null || to == null || !from.isBefore(to)) {
            return List.of();
        }

        List<BookingDemandEvent> eventsInWindow = bookingEvents.stream()
                .filter(e -> !e.getTimestamp().isBefore(from) && e.getTimestamp().isBefore(to))
                .collect(Collectors.toList());

        Map<Long, List<BookingDemandEvent>> byCity = eventsInWindow.stream()
                .collect(Collectors.groupingBy(BookingDemandEvent::getCityId));

        List<CityDemandStatsDTO> result = new ArrayList<>();

        for (Map.Entry<Long, List<BookingDemandEvent>> entry : byCity.entrySet()) {
            Long cityId = entry.getKey();
            List<BookingDemandEvent> cityEvents = entry.getValue();

            Map<Instant, Long> byHour = new HashMap<>();
            for (BookingDemandEvent event : cityEvents) {
                Instant hourStart = event.getTimestamp().truncatedTo(ChronoUnit.HOURS);
                byHour.merge(hourStart, 1L, Long::sum);
            }

            Instant busiestHourStart = null;
            long busiestCount = 0L;
            for (Map.Entry<Instant, Long> bucket : byHour.entrySet()) {
                if (bucket.getValue() > busiestCount || busiestHourStart == null) {
                    busiestHourStart = bucket.getKey();
                    busiestCount = bucket.getValue();
                }
            }

            result.add(new CityDemandStatsDTO(cityId, cityEvents.size(), busiestHourStart, busiestCount));
        }

        result.sort(Comparator.comparingLong(CityDemandStatsDTO::getTotalBookings).reversed());
        return result;
    }
}

