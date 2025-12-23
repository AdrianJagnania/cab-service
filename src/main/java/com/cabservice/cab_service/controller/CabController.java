package com.cabservice.cab_service.controller;

import com.cabservice.cab_service.dto.CreateCabRequestDTO;
import com.cabservice.cab_service.dto.UpdateCabLocationRequestDTO;
import com.cabservice.cab_service.dto.UpdateCabStateRequestDTO;
import com.cabservice.cab_service.entity.Cab;
import com.cabservice.cab_service.service.CabService;
import org.springframework.beans.factory.annotation.Autowired;
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


    @PostMapping
    public ResponseEntity<String> registerCab(@RequestBody CreateCabRequestDTO request) {
        cabService.registerCab(request.getCityId());
        return ResponseEntity.ok("Cab registered successfully");
    }

    @PutMapping("/{id}/state")
    public ResponseEntity<String> updateCabState(
            @PathVariable Long id,
            @RequestBody UpdateCabStateRequestDTO request) {

        cabService.updateState(id, request.getState());
        return ResponseEntity.ok("Cab state updated successfully");
    }

    @PutMapping("/{id}/location")
    public ResponseEntity<String> updateCabLocation(
            @PathVariable Long id,
            @RequestBody UpdateCabLocationRequestDTO request) {

        cabService.updateLocation(id, request.getCityId());
        return ResponseEntity.ok("Cab location updated successfully");
    }

    @GetMapping("/getAllCabs")
    public ResponseEntity<List<Cab>> getAllCabs() {

        List<Cab> cabs = cabService.getAllCabs();
        return ResponseEntity.ok(cabs);
    }
}
