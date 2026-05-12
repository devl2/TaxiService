package com.example.TripService.record;

import com.example.TripService.enums.TripStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record Trip(Integer id, Integer passengerId, Integer driverId, TripStatus status, String origin, String destination, String createdAt, String endedAt) {
    public static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public static Trip create(Integer passengerId, String origin, String destination){
        String createdAt = LocalDateTime.now().format(ISO_FORMATTER);
        return new Trip(null, passengerId, null , TripStatus.CREATED , origin, destination, createdAt, null);
    }

}
