package com.cabservice.cab_service.dto;

import lombok.Data;

@Data
public class UpdateCabLocationRequestDTO {
    private Long cabId;
    private Long cityId;
}
