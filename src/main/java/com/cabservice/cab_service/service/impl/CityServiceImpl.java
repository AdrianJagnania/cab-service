package com.cabservice.cab_service.service.impl;

import com.cabservice.cab_service.entity.City;
import com.cabservice.cab_service.repository.CityRepository;
import com.cabservice.cab_service.service.CityService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class CityServiceImpl implements CityService {
    private static final Logger logger = Logger.getLogger(String.valueOf(CityServiceImpl.class));
    private final CityRepository cityRepository;

    public CityServiceImpl(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    public City createCity(Long cityId) {
        if (cityRepository.findByCityId(cityId).isPresent()) {
            logger.info("city id exists: " + cityId);
            throw new IllegalArgumentException("City already exists");
        }
        City city = new City(cityId);
        cityRepository.save(city);
        return city;
    }

    @Override
    public City getCity(Long cityId) {
        return cityRepository.findByCityId(cityId)
                .orElseThrow(() -> new IllegalArgumentException("City not found"));
    }

    @Override
    public List<City> getAllCities() {
        return cityRepository.findAll();
    }
}
