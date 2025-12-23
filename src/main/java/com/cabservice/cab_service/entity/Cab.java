package com.cabservice.cab_service.entity;

import com.cabservice.cab_service.enums.CabState;
import lombok.Data;

import java.time.Instant;

@Data
public class Cab {
    private Long cabId;
    private CabState state;
    private Long cityId; // null when ON_TRIP
    private Instant lastStateChangeTime;

    public static Cab createIdleCab(Long cityId, Instant now) {
        Cab cab = new Cab();
        cab.setState(CabState.IDLE);
        cab.setCityId(cityId);
        cab.setLastStateChangeTime(now);
        return cab;
    }
}
