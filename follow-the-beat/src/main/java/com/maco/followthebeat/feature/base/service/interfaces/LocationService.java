package com.maco.followthebeat.feature.base.service.interfaces;

import com.maco.followthebeat.feature.base.entity.Location;

import java.util.List;
import java.util.UUID;

public interface LocationService {
    Location getLocationById(UUID locationId);
    List<Location> getAllLocations();
}