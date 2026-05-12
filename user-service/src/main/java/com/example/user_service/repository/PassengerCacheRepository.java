package com.example.user_service.repository;

import com.example.user_service.entity.PassengerCache;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassengerCacheRepository extends JpaRepository<PassengerCache, Long> {
}