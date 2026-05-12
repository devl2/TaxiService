package com.example.user_service.service;

import com.example.user_service.dto.DriverResponse;
import com.example.user_service.entity.Driver;
import com.example.user_service.entity.Passenger;
import com.example.user_service.repository.DriverRepository;
import enums.DriverStatus;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverService {

    private final DriverRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final AvailableDriverCacheService availableDriverCacheService;

    public DriverService(DriverRepository repository,
                         PasswordEncoder passwordEncoder,
                         AvailableDriverCacheService availableDriverCacheService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.availableDriverCacheService = availableDriverCacheService;
    }

    @CacheEvict(value = "availableDrivers", allEntries = true)
    public Driver createDriver(Driver driver) {
        driver.setPassword(passwordEncoder.encode(driver.getPassword()));
        driver.setStatus(DriverStatus.UNAVAILABLE);
        return repository.save(driver);
    }

    @CacheEvict(value = "availableDrivers", allEntries = true)
    public String updateStatus(Long driverId, String status) {
        Driver driver = repository.findById(driverId).orElse(null);

        if (driver == null) {
            throw new IllegalArgumentException("Водитель не найден");
        }

        driver.setStatus(DriverStatus.valueOf(status));
        repository.save(driver);

        return "Статус водителя " + driverId + ": " + status;
    }

    @Cacheable(value = "drivers", key = "#driverId")
    public Driver getDriver(Long driverId) {
        return repository.findById(driverId).orElse(null);
    }

    @Cacheable(value = "driverStatus", key = "#driverId")
    public String getStatus(Long driverId) {
        Driver driver = repository.findById(driverId).orElse(null);

        if (driver == null) {
            throw new IllegalArgumentException("Водитель не найден");
        }

        return driver.getStatus().name();
    }

    public boolean isAvailable(Long driverId) {
        Driver driver = repository.findById(driverId).orElse(null);

        return driver != null && driver.getStatus() == DriverStatus.AVAILABLE;
    }

    @CacheEvict(value = "availableDrivers", allEntries = true)
    public Driver assignAvailableDriver() {

        List<Long> driverIds = availableDriverCacheService.getAvailableDriverIds();
        Long driverId = (driverIds == null || driverIds.isEmpty()) ? null : driverIds.get(0);

        if (driverId == null) {
            return null;
        }

        Driver driver = repository.findById(driverId).orElse(null);
        if (driver == null) {
            return null;
        }

        driver.setStatus(DriverStatus.BUSY);

        return repository.save(driver);
    }

    public Driver save(Driver driver) {
        return repository.save(driver);
    }

    public DriverResponse getByEmail(String email) {
        Driver driver = repository.findTopByEmailOrderByIdDesc(email)
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        return new DriverResponse(
                driver.getId(),
                driver.getEmail(),
                driver.getPassword(),
                driver.getStatus().name()
        );
    }

    public List<Long> getAvailableDriversCached() {
        return availableDriverCacheService.getAvailableDriverIds();
    }


}