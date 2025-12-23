package com.cabservice.cab_service.controller;

import com.cabservice.cab_service.dto.UpdateCabLocationRequestDTO;
import com.cabservice.cab_service.dto.UpdateCabStateRequestDTO;
import com.cabservice.cab_service.entity.Cab;
import com.cabservice.cab_service.service.CabService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/cabs")
public class CabController {
    private final CabService cabService;

    public CabController(CabService cabService) {
        this.cabService = cabService;
    }


    @PostMapping("/registerCab")
    public ResponseEntity<String> registerCab(@RequestParam Long cityId) {
        cabService.registerCab(cityId);
        return ResponseEntity.ok("Cab registered successfully");
    }

    @PutMapping("/updateState")
    public ResponseEntity<String> updateCabState(
            @RequestBody UpdateCabStateRequestDTO request) {
        cabService.updateState(request.getCabId(), request.getState());
        return ResponseEntity.ok("Cab state updated successfully");
    }

    @PutMapping("/updateLocation")
    public ResponseEntity<String> updateCabLocation(
            @RequestBody UpdateCabLocationRequestDTO request) {

        cabService.updateLocation(request.getCabId(), request.getCityId());
        return ResponseEntity.ok("Cab location updated successfully");
    }

    @GetMapping("/getAllCabs")
    public ResponseEntity<List<Cab>> getAllCabs() {

        List<Cab> cabs = cabService.getAllCabs();
        return ResponseEntity.ok(cabs);
    }
}
