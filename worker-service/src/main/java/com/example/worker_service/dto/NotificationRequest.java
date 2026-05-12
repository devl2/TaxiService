package com.example.worker_service.dto;

public record NotificationRequest(
        Long tripId,
        String recipientType,
        Long recipientId,
        String message
) {
}