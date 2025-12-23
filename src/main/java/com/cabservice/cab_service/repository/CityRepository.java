package com.cabservice.cab_service.repository;

import com.cabservice.cab_service.entity.City;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository {
    void save(City city);
    Optional<City> findByCityId(Long cityId);
    List<City> findAll();
    Optional<City> findByCityName(String cityName);
}
