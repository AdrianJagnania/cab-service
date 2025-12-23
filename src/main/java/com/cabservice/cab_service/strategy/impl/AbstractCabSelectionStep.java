package com.cabservice.cab_service.strategy.impl;

import com.cabservice.cab_service.NoCabAvailableException;
import com.cabservice.cab_service.entity.Cab;
import com.cabservice.cab_service.strategy.CabSelectionStep;

import java.util.List;


public abstract class AbstractCabSelectionStep implements CabSelectionStep {

    private final CabSelectionStep next;

    protected AbstractCabSelectionStep(CabSelectionStep next) {
        this.next = next;
    }

    protected Cab delegate(List<Cab> cabs) {
        if (next == null) {
            throw new NoCabAvailableException("No cab available");
        }
        return next.handle(cabs);
    }
}

