package com.cabservice.cab_service.repository.impl;

import com.cabservice.cab_service.entity.Cab;
import com.cabservice.cab_service.enums.CabState;
import com.cabservice.cab_service.repository.CabRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryCabRepository implements CabRepository {
    private final Map<Long, Cab> cabStore = new ConcurrentHashMap<>();
    private final AtomicLong idSequence = new AtomicLong(1);

    @Override
    public void save(Cab cab) {
        if (cab.getCabId() == null) {
            cab.setCabId(idSequence.getAndIncrement());
        }
        cabStore.put(cab.getCabId(), cab);
    }

    @Override
    public Optional<Cab> findByCabId(Long cabId) {
        return Optional.ofNullable(cabStore.get(cabId));
    }

    @Override
    public List<Cab> findAll() {
        return new ArrayList<>(cabStore.values());
    }

    @Override
    public List<Cab> findByStateAndCityId(CabState cabState, Long cityId) {
        List<Cab> result = new ArrayList<>();
        for (Cab cab : cabStore.values()) {
            if (cabState.equals(cab.getState()) && cityId != null && cityId.equals(cab.getCityId())) {
                result.add(cab);
            }
        }
        return result;
    }
}
