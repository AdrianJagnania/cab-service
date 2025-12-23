package com.cabservice.cab_service.event;

import com.cabservice.cab_service.service.AnalyticsService;
import org.springframework.stereotype.Component;

@Component
public class CabStateChangedEventHandler implements DomainEventHandler<CabStateChangedEvent> {

    private final AnalyticsService analyticsService;

    public CabStateChangedEventHandler(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @Override
    public String getSupportedEventName() {
        return CabStateChangedEvent.EVENT_NAME;
    }

    @Override
    public void handle(CabStateChangedEvent event) {
        analyticsService.recordCabStateChange(
                event.getCabId(),
                event.getNewState(),
                event.getCityId(),
                event.getTimestamp());
    }
}

