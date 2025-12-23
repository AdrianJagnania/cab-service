package com.cabservice.cab_service.strategy.impl;

import com.cabservice.cab_service.entity.Cab;

import java.util.List;

/**
 * A single step in the cab-selection chain. It can return a cab or delegate to the next step.
 */
public interface CabSelectionStep {
    Cab handle(List<Cab> cabs);
}

