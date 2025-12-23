package com.cabservice.cab_service;

import com.cabservice.cab_service.entity.Cab;
import com.cabservice.cab_service.enums.CabState;
import com.cabservice.cab_service.strategy.impl.LongestIdleCabStrategy;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LongestIdleCabStrategyTest {

    private final LongestIdleCabStrategy strategy = new LongestIdleCabStrategy();

    @Test
    void assignCab_picksCabWithOldestLastStateChangeTime() {
        Instant now = Instant.now();

        Cab cab1 = new Cab();
        cab1.setCabId(1L);
        cab1.setState(CabState.IDLE);
        cab1.setLastStateChangeTime(now.minusSeconds(60)); // idle for 1 min

        Cab cab2 = new Cab();
        cab2.setCabId(2L);
        cab2.setState(CabState.IDLE);
        cab2.setLastStateChangeTime(now.minusSeconds(300)); // idle for 5 min (longest)

        Cab cab3 = new Cab();
        cab3.setCabId(3L);
        cab3.setState(CabState.IDLE);
        cab3.setLastStateChangeTime(now.minusSeconds(120)); // idle for 2 min

        Cab assigned = strategy.assignCab(Arrays.asList(cab1, cab2, cab3));

        assertThat(assigned.getCabId()).isEqualTo(2L);
    }

    @Test
    void assignCab_picksOneOfOldestWhenTie() {
        Instant base = Instant.now();

        Cab cab1 = new Cab();
        cab1.setCabId(1L);
        cab1.setLastStateChangeTime(base.minusSeconds(300));

        Cab cab2 = new Cab();
        cab2.setCabId(2L);
        cab2.setLastStateChangeTime(base.minusSeconds(300));

        List<Cab> cabs = Arrays.asList(cab1, cab2);

        Cab assigned = strategy.assignCab(cabs);

        assertThat(assigned.getCabId()).isIn(1L, 2L);
    }

    @Test
    void assignCab_emptyListThrows() {
        assertThrows(RuntimeException.class, () -> strategy.assignCab(Collections.emptyList()));
    }
}

