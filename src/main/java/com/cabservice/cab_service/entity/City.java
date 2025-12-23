package com.cabservice.cab_service.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
public class City {
    private Long cityId;
    private String name;


    public City(Long cityId) {
    }
}
