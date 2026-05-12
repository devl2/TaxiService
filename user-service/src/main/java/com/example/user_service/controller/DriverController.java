package com.example.user_service.controller;

import com.example.user_service.dto.DriverResponse;
import com.example.user_service.dto.LoginRequest;
import com.example.user_service.entity.Driver;
import com.example.user_service.entity.Passenger;
import com.example.user_service.repository.DriverRepository;
import com.example.user_service.service.DriverService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/drivers")
public class DriverController {

    private final DriverService driverService;

    private final DriverRepository driverRepository;

    public DriverController(DriverService driverService,
                            DriverRepository driverRepository) {
        this.driverService = driverService;
        this.driverRepository = driverRepository;
    }

    @PostMapping
    public Driver registerDriver(@RequestBody Driver request){
        return driverService.createDriver(request);
    }

    @GetMapping("/{id}")
    public Driver getDriver(@PathVariable Long id){
        return driverService.getDriver(id);
    }

    @PatchMapping("/{id}/status")
    public String patchStatus(@PathVariable Long id, @RequestBody String status){
        return driverService.updateStatus(id, status);
    }

    @GetMapping("/assign")
    public Driver assignDriver() {
        return driverService.assignAvailableDriver();
    }

    @GetMapping("/available")
    public List<Long> getAvailableDrivers() {
        return driverService.getAvailableDriversCached();
    }

    @GetMapping("/email")
    public DriverResponse getByEmail(@RequestParam String email){
        return driverService.getByEmail(email);
    }
}