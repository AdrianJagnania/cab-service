package com.cabservice.cab_service.dto;

import com.cabservice.cab_service.enums.CabState;
import lombok.Data;

@Data
public class UpdateCabStateRequestDTO {
    private Long CabId;
    private CabState state;
}
