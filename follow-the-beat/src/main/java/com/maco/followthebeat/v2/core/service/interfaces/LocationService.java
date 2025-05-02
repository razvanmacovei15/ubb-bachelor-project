package com.maco.followthebeat.v2.core.service.interfaces;

import com.maco.followthebeat.v2.core.entity.Location;

import java.util.List;
import java.util.UUID;

public interface LocationService {
    Location getLocationById(UUID locationId);
    List<Location> getAllLocations();
}