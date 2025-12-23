package com.cabservice.cab_service.service.impl;

import com.cabservice.cab_service.entity.Cab;
import com.cabservice.cab_service.entity.City;
import com.cabservice.cab_service.enums.CabState;
import com.cabservice.cab_service.repository.CabRepository;
import com.cabservice.cab_service.repository.CityRepository;
import com.cabservice.cab_service.service.CabService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class CabServiceImpl implements CabService {
    private final CabRepository cabRepository;
    private final CityRepository cityRepository;

    public CabServiceImpl(CabRepository cabRepository, CityRepository cityRepository) {
        this.cabRepository = cabRepository;
        this.cityRepository = cityRepository;
    }

    // Register a new cab
    @Override
    public void registerCab(Long cabId, Long cityId) {

        City city = cityRepository.findByCityId(cityId)
                .orElseThrow(() -> new IllegalArgumentException("City not found"));

        Cab cab = new Cab();
        cab.setCabId(cabId);
        cab.setState(CabState.IDLE);
        cab.setCity(city);
        cab.setLastStateChangeTime(Instant.now());

        cabRepository.save(cab);
    }

    // Update cab state (IDLE / ON_TRIP)
    @Override
    public void updateState(Long cabId, CabState newState) {

        Cab cab = cabRepository.findByCabId(cabId)
                .orElseThrow(() -> new IllegalArgumentException("Cab not found"));

        if (cab.getState() == newState) {
            return; // no-op
        }

        cab.setState(newState);
        cab.setLastStateChangeTime(Instant.now());

        if (newState == CabState.ON_TRIP) {
            cab.setCity(null);
        }

        cabRepository.save(cab);
    }

    // Update cab location (only allowed when IDLE)
    @Override
    public void updateLocation(Long cabId, Long cityId) {

        Cab cab = cabRepository.findByCabId(cabId)
                .orElseThrow(() -> new IllegalStateException("Cab not found"));

        if (cab.getState() != CabState.IDLE) {
            throw new IllegalStateException("Cannot update location while cab is ON_TRIP");
        }

        City city = cityRepository.findByCityId(cityId)
                .orElseThrow(() -> new IllegalArgumentException("City not found"));

        cab.setCity(city);
        cabRepository.save(cab);
    }

    @Override
    public List<Cab> getAllCabs() {
        return cabRepository.findAll();
    }
}
