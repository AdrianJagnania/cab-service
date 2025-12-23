package com.cabservice.cab_service.controller;

import com.cabservice.cab_service.entity.City;
import com.cabservice.cab_service.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cities")
public class CityController {

    @Autowired
    private CityService cityService;

    @PostMapping("/create")
    public ResponseEntity<String> createCity(@RequestParam String cityName) {
        cityService.createCity(cityName);
        return ResponseEntity.ok("City created successfully");
    }

    @GetMapping("/getAllCities")
    public ResponseEntity<List<City>> getAllCities() {
        List<City> cities = cityService.getAllCities();
        return ResponseEntity.ok(cities);
    }

    @GetMapping("/getCity")
    public ResponseEntity<City> getCityById(@RequestParam Long cityId) {
        City city = cityService.getCity(cityId);
        return ResponseEntity.ok(city);
    }
}
