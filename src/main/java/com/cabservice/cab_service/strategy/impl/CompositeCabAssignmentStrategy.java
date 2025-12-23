package com.cabservice.cab_service.strategy.impl;

import com.cabservice.cab_service.entity.Cab;
import com.cabservice.cab_service.strategy.CabAssignmentStrategy;
import com.cabservice.cab_service.strategy.CabSelectionStep;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Chain of responsibility for cab assignment:
 * 1) OldestIdleSelectionStep narrows to the longest-idle cab(s).
 * 2) RandomTieBreakerStep breaks ties if more than one cab shares the oldest timestamp.
 *
 * Any NoCabAvailableException thrown by any step is propagated immediately.
 */
@Primary
@Component
public class CompositeCabAssignmentStrategy implements CabAssignmentStrategy {

    private final CabSelectionStep chain;

    public CompositeCabAssignmentStrategy() {
        this.chain = new OldestIdleSelectionStep(new RandomTieBreakerStep());
    }

    // Visible for testing
    CompositeCabAssignmentStrategy(CabSelectionStep chain) {
        this.chain = chain;
    }

    @Override
    public Cab assignCab(List<Cab> cabs) {
        return chain.handle(cabs);
    }
}

