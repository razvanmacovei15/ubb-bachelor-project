package com.maco.followthebeat.v2.core.service.impl;

import com.maco.followthebeat.v2.core.entity.Venue;
import com.maco.followthebeat.v2.core.generics.BaseCrudServiceImpl;
import com.maco.followthebeat.v2.core.repo.VenueRepo;
import com.maco.followthebeat.v2.core.service.interfaces.VenueService;
import org.springframework.stereotype.Service;

@Service
public class VenueServiceImpl extends BaseCrudServiceImpl<Venue> implements VenueService {
    public VenueServiceImpl(VenueRepo venueRepo) {
        super(venueRepo);
    }
}
