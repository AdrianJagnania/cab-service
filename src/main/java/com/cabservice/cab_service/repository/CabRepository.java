package com.cabservice.cab_service.repository;

import com.cabservice.cab_service.entity.Cab;
import com.cabservice.cab_service.enums.CabState;

import java.util.List;
import java.util.Optional;

public interface CabRepository {
    void save(Cab cab);
    Optional<Cab> findByCabId(Long cabId);
    List<Cab> findAll();

    List<Cab> findByStateAndCityId(CabState cabState,Long cityId);
}
