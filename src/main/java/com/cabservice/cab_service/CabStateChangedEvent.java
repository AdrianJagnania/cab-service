package com.cabservice.cab_service;

import com.cabservice.cab_service.enums.CabState;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class CabStateChangedEvent implements DomainEvent {
    public static final String EVENT_NAME = "CAB_STATE_CHANGED";
    private Long cabId;
    private CabState previousState;
    private CabState newState;
    private Long cityId;
    private Instant timestamp;
    @Override
    public String getEventName() {
        return EVENT_NAME;
    }
}
