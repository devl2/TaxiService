package com.example.TripService.dto;

public record CreateTripRequest(
        Long passengerId,
        String origin,
        String destination
) {}
