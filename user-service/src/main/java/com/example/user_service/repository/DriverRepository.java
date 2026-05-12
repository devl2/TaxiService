package com.example.user_service.repository;

import com.example.user_service.entity.Driver;
import enums.DriverStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface DriverRepository extends JpaRepository<Driver, Long> {
    Optional<Driver> findTopByEmailOrderByIdDesc(String email);
    Optional<Driver> findFirstByStatus(DriverStatus status);
    List<Driver> findAllByStatus(DriverStatus status);

    @Query("select d.id from Driver d where d.status = :status")
    List<Long> findAllIdsByStatus(DriverStatus status);
}
