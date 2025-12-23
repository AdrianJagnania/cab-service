package com.cabservice.cab_service.entity;

import com.cabservice.cab_service.enums.CabState;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class CabStateHistoryEntry {
    private Long cabId;
    private CabState state;
    private Long cityId;
    private Instant timestamp;
}

