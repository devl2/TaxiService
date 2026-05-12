package com.example.TripService.repository;

import com.example.TripService.entity.PassengerCache;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassengerCacheRepository extends JpaRepository<PassengerCache, Long> {
}
