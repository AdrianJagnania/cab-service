package com.cabservice.cab_service.repository.impl;

import com.cabservice.cab_service.entity.Cab;
import com.cabservice.cab_service.repository.CabRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryCabRepository implements CabRepository {
    private final Map<Long, Cab> cabStore = new ConcurrentHashMap<>();
    @Override
    public void save(Cab cab) {
        if(cab.getCabId() == null) {
            Long newId = (long) cabStore.size() + 1;
            cab.setCabId(newId);
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
}
