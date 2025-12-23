package com.cabservice.cab_service.strategy.impl;

import com.cabservice.cab_service.NoCabAvailableException;
import com.cabservice.cab_service.entity.Cab;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Tie-breaker: picks a random cab from the provided list.
 */
public class RandomTieBreakerStep extends AbstractCabSelectionStep {

    public RandomTieBreakerStep() {
        super(null);
    }

    @Override
    public Cab handle(List<Cab> cabs) {
        if (cabs == null || cabs.isEmpty()) {
            throw new NoCabAvailableException("No cab available");
        }
        int index = ThreadLocalRandom.current().nextInt(cabs.size());
        return cabs.get(index);
    }
}

