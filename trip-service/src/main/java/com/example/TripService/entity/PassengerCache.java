package com.example.TripService.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "passenger_cache")
public class PassengerCache {

    @Id
    private Long passengerId;

    public PassengerCache() {}

    public PassengerCache(Long passengerId) {
        this.passengerId = passengerId;
    }

    public Long getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(Long passengerId) {
        this.passengerId = passengerId;
    }
}

