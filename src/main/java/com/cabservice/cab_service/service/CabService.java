package com.cabservice.cab_service.service;

import com.cabservice.cab_service.entity.Cab;
import com.cabservice.cab_service.enums.CabState;

import java.util.List;

public interface CabService {
    Cab registerCab(Long cityId);
    void updateState(Long cabId, CabState newState);
    void updateLocation(Long cabId, Long cityId);
    List<Cab> getAllCabs();

}
