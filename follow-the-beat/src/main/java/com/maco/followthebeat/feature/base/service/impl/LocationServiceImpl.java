package com.maco.followthebeat.feature.base.service.impl;

import com.maco.followthebeat.feature.base.entity.Location;
import com.maco.followthebeat.feature.base.repo.LocationRepo;
import com.maco.followthebeat.feature.base.service.interfaces.LocationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
@Service
public class LocationServiceImpl implements LocationService {
    private final LocationRepo locationRepo;

    public LocationServiceImpl(LocationRepo locationRepo) {
        this.locationRepo = locationRepo;
    }

    @Override
    public Location getLocationById(UUID locationId) {
        return locationRepo.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Location not found with id: " + locationId));
    }

    @Override
    public List<Location> getAllLocations() {
        return locationRepo.findAll();
    }
}
