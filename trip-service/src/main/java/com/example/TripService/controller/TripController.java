package com.example.TripService.controller;

import com.example.TripService.dto.CreateTripRequest;
import com.example.TripService.dto.RateTripRequest;
import com.example.TripService.dto.TripCreatedEvent;
import com.example.TripService.dto.TripStats;
import com.example.TripService.entity.Trip;
import com.example.TripService.enums.TripStatus;
import com.example.TripService.messaging.TripEventProducer;
import com.example.TripService.service.TripService;
import jakarta.annotation.PostConstruct;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trips")
public class TripController {

    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @PostConstruct
    public void init() {
        System.out.println("PassengerEventListener STARTED");
    }

    @PostMapping
    public Trip createTrip(@RequestBody CreateTripRequest request) {
        return tripService.createTrip(
                request.passengerId(),
                request.origin(),
                request.destination()
        );
    }

    @GetMapping("/{id}")
    public Trip getTrip(@PathVariable Long id) {
        return tripService.getTrip(id);
    }

    @GetMapping
    public List<Trip> getTripsByPassenger(@RequestParam Long passengerId) {
        return tripService.getTripsByPassenger(passengerId);
    }

    @PostMapping("/{id}/rate")
    public Trip rateTrip(@PathVariable Long id, @RequestBody RateTripRequest request) {
        return tripService.rateTrip(id, request.rating());
    }

    @PatchMapping("/{id}/status")
    public Trip updateStatus(@PathVariable Long id, @RequestBody TripStatus status) {
        return tripService.updateStatus(id, status);
    }
}
