package com.cabservice.cab_service.strategy.impl;

import com.cabservice.cab_service.event.NoCabAvailableException;
import com.cabservice.cab_service.entity.Cab;
import com.cabservice.cab_service.strategy.CabSelectionStep;

import java.util.Comparator;
import java.util.List;


public class OldestIdleSelectionStep extends AbstractCabSelectionStep {

    public OldestIdleSelectionStep(CabSelectionStep next) {
        super(next);
    }

    @Override
    public Cab handle(List<Cab> cabs) {
        if (cabs == null || cabs.isEmpty()) {
            throw new NoCabAvailableException("No cab available");
        }

        var oldestTime = cabs.stream()
                .map(Cab::getLastStateChangeTime)
                .min(Comparator.naturalOrder())
                .orElseThrow(() -> new NoCabAvailableException("No cab available"));

        var oldestCabs = cabs.stream()
                .filter(cab -> cab.getLastStateChangeTime().equals(oldestTime))
                .toList();

        if (oldestCabs.size() == 1) {
            return oldestCabs.get(0);
        }

        // Delegate tie-breaking to the next step with only the tied cabs.
        return delegate(oldestCabs);
    }
}

