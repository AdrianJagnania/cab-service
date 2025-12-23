package com.cabservice.cab_service.dto;

import lombok.Data;

@Data
public class CreateBookingRequestDTO {
    private Long sourceCityId;
    private Long destinationCityId;
}
