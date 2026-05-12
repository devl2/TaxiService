package com.example.TripService.dto;

public record TripCreatedEvent(
        Long tripId,
        Long passengerId
) {}