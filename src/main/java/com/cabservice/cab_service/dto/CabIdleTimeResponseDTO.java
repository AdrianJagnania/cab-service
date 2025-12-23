package com.cabservice.cab_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class CabIdleTimeResponseDTO {
    private Long cabId;
    private Instant from;
    private Instant to;
    private long idleSeconds;
}

