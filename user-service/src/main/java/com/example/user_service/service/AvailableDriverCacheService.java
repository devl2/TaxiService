package com.example.user_service.service;

import com.example.user_service.repository.DriverRepository;
import enums.DriverStatus;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AvailableDriverCacheService {

    private final DriverRepository driverRepository;

    public AvailableDriverCacheService(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    @Cacheable("availableDrivers")
    public List<Long> getAvailableDriverIds() {
        return driverRepository.findAllIdsByStatus(DriverStatus.AVAILABLE);
    }
}

