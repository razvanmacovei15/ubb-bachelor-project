package com.maco.followthebeat.v2.core.service.impl;

import com.maco.followthebeat.v2.core.mappers.*;
import com.maco.followthebeat.v2.core.service.interfaces.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public abstract class SuperService {
    @Autowired protected ArtistService artistService;
    @Autowired protected ConcertService concertService;
    @Autowired protected FestivalService festivalService;
    @Autowired protected ScheduleService scheduleService;
    @Autowired protected StageService stageService;
    @Autowired protected VenueService venueService;

    @Autowired protected FestivalMapper festivalMapper;
    @Autowired protected StageMapper stageMapper;
    @Autowired protected ArtistMapper artistMapper;
    @Autowired protected ScheduleMapper scheduleMapper;
    @Autowired protected ConcertMapper concertMapper;

}
