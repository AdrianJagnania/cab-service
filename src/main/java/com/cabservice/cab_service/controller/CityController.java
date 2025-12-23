package com.cabservice.cab_service.controller;

import com.cabservice.cab_service.dto.CreateCityRequestDTO;
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

    // POST /cities → Create a city
    @PostMapping("/create")
    public ResponseEntity<String> createCity(@RequestBody CreateCityRequestDTO request) {
        cityService.createCity(request.getCityId());
        return ResponseEntity.ok("City created successfully");
    }

    // GET /cities → List all cities
    @GetMapping
    public ResponseEntity<List<City>> getAllCities() {
        List<City> cities = cityService.getAllCities();
        return ResponseEntity.ok(cities);
    }

    // GET /cities/{id} → Get city by ID
    @GetMapping("/{id}")
    public ResponseEntity<City> getCityById(@PathVariable Long id) {
        City city = cityService.getCity(id);
        return ResponseEntity.ok(city);
    }
}
