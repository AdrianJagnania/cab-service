package com.cabservice.cab_service.entity;

import com.cabservice.cab_service.enums.CabState;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.Instant;

@Getter
@Setter
public class Cab {
    Long cabId;
    CabState state;
    City city; // null when ON_TRIP
    Instant lastStateChangeTime;
}
