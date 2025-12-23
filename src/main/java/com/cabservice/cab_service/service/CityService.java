package com.cabservice.cab_service.service;

import com.cabservice.cab_service.entity.City;

import java.util.List;

public interface CityService {
    City createCity(Long cityId);
    City getCity(Long cityId);
    List<City> getAllCities();
}
