package com.maco.followthebeat.feature.base.service.impl;

import com.maco.followthebeat.feature.base.entity.Venue;
import com.maco.followthebeat.feature.base.generics.BaseCrudServiceImpl;
import com.maco.followthebeat.feature.base.repo.VenueRepo;
import com.maco.followthebeat.feature.base.service.interfaces.VenueService;
import org.springframework.stereotype.Service;

@Service
public class VenueServiceImpl extends BaseCrudServiceImpl<Venue> implements VenueService {
    public VenueServiceImpl(VenueRepo venueRepo) {
        super(venueRepo);
    }
}
