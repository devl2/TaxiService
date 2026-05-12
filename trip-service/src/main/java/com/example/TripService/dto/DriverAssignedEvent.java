package com.example.TripService.dto;

public record DriverAssignedEvent(
        Long tripId,
        Long driverId
) {}
