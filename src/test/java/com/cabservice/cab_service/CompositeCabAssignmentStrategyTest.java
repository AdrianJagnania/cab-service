package com.cabservice.cab_service;

import com.cabservice.cab_service.NoCabAvailableException;
import com.cabservice.cab_service.entity.Cab;
import com.cabservice.cab_service.strategy.impl.CompositeCabAssignmentStrategy;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CompositeCabAssignmentStrategyTest {

    @Test
    void picksOldestIdleWhenSingleOldest() {
        CompositeCabAssignmentStrategy strategy = new CompositeCabAssignmentStrategy();
        Instant now = Instant.now();

        Cab c1 = new Cab();
        c1.setCabId(1L);
        c1.setLastStateChangeTime(now.minusSeconds(60));

        Cab c2 = new Cab();
        c2.setCabId(2L);
        c2.setLastStateChangeTime(now.minusSeconds(300)); // longest idle

        Cab chosen = strategy.assignCab(Arrays.asList(c1, c2));
        assertThat(chosen.getCabId()).isEqualTo(2L);
    }

    @Test
    void tieIsBrokenRandomlyAmongOldest() {
        CompositeCabAssignmentStrategy strategy = new CompositeCabAssignmentStrategy();
        Instant now = Instant.now();

        Cab c1 = new Cab();
        c1.setCabId(1L);
        c1.setLastStateChangeTime(now.minusSeconds(300));

        Cab c2 = new Cab();
        c2.setCabId(2L);
        c2.setLastStateChangeTime(now.minusSeconds(300));

        Cab chosen = strategy.assignCab(Arrays.asList(c1, c2));
        assertThat(chosen.getCabId()).isIn(1L, 2L);
    }

    @Test
    void throwsWhenNoCabs() {
        CompositeCabAssignmentStrategy strategy = new CompositeCabAssignmentStrategy();
        assertThrows(NoCabAvailableException.class, () -> strategy.assignCab(Collections.emptyList()));
    }
}
