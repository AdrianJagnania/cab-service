package com.cabservice.cab_service.event;

import com.cabservice.cab_service.service.AnalyticsService;
import org.springframework.stereotype.Component;

@Component
public class BookingCreatedEventHandler implements DomainEventHandler<BookingCreatedEvent> {

    private final AnalyticsService analyticsService;

    public BookingCreatedEventHandler(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @Override
    public String getSupportedEventName() {
        return BookingCreatedEvent.EVENT_NAME;
    }

    @Override
    public void handle(BookingCreatedEvent event) {
        analyticsService.recordBookingDemand(
                event.getSourceCityId(),
                event.getTimestamp());
    }
}

