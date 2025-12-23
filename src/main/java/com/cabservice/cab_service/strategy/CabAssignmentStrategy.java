package com.cabservice.cab_service.strategy;

import com.cabservice.cab_service.entity.Cab;

import java.util.List;

public interface CabAssignmentStrategy {
    Cab assignCab(List<Cab> cabs);
}
