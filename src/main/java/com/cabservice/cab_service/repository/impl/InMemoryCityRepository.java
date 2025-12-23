package com.cabservice.cab_service.repository.impl;

import com.cabservice.cab_service.entity.City;
import com.cabservice.cab_service.repository.CityRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryCityRepository implements CityRepository {
    private final Map<Long, City> cityStore = new ConcurrentHashMap<>();

    @Override
    public void save(City city) {
        cityStore.put(city.getCityId(), city);
    }

    @Override
    public Optional<City> findByCityId(Long cityId) {
        return Optional.ofNullable(cityStore.get(cityId));
    }

    @Override
    public List<City> findAll() {
        return new ArrayList<>(cityStore.values());
    }

    @Override
    public Optional<City> findByCityName(String cityName) {
        return cityStore.values()
                .stream()
                .filter(city -> city.getName().equalsIgnoreCase(cityName))
                .findFirst();
    }
}
