package com.cabservice.cab_service.service;

import com.cabservice.cab_service.entity.City;

import java.util.List;

public interface CityService {
    City createCity(String cityName);
    City getCity(Long cityId);
    List<City> getAllCities();
}
