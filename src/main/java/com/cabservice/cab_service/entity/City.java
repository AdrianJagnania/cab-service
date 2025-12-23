package com.cabservice.cab_service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class City {
    private Long cityId;
    private String name;

    public City(String cityName) {
        this.name = cityName;
    }
}
