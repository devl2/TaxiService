package com.example.user_service.controller;

import com.example.user_service.dto.LoginRequest;
import com.example.user_service.entity.Driver;
import com.example.user_service.entity.Passenger;
import com.example.user_service.service.PassengerService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/passengers")
public class PassengerController {

    private final PassengerService passengerService;

    public PassengerController(PassengerService passengerService) {
        this.passengerService = passengerService;
    }

    @PostMapping
    public Passenger registerPassenger(@RequestBody Passenger request){
        return passengerService.createPassenger(request);
    }

    @GetMapping("/{id}")
    public Passenger getPassenger(@PathVariable Long id){
        return passengerService.getPassenger(id);
    }

    @GetMapping("/email")
    public Passenger getByEmail(@RequestParam String email){
        return passengerService.getByEmail(email);
    }

}