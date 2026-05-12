package com.example.TripService.dto;

import com.example.TripService.enums.TripStatus;

public record TripStatusChangedEvent(
        Long tripId,
        TripStatus status,
        Long passengerId,
        Long driverId
) {}