package com.example.TripService.dto;

public record NotificationRequest(
        Long tripId,
        String recipientType,
        Long recipientId,
        String message
) {
}
