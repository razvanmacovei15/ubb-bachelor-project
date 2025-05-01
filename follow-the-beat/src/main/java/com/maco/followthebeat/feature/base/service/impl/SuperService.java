package com.maco.followthebeat.feature.base.service.impl;

import com.maco.followthebeat.feature.base.mappers.ArtistMapper;
import com.maco.followthebeat.feature.base.mappers.FestivalMapper;
import com.maco.followthebeat.feature.base.mappers.ScheduleMapper;
import com.maco.followthebeat.feature.base.mappers.StageMapper;
import com.maco.followthebeat.feature.base.service.interfaces.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public abstract class SuperService {
    @Autowired protected ArtistServiceImpl artistService;
    @Autowired protected ConcertServiceImpl concertService;
    @Autowired protected FestivalService festivalService;
    @Autowired protected ScheduleService scheduleService;
    @Autowired protected StageServiceImpl stageService;
    @Autowired protected VenueService venueService;

    @Autowired protected FestivalMapper festivalMapper;
    @Autowired protected StageMapper stageMapper;
    @Autowired protected ArtistMapper artistMapper;
    @Autowired protected ScheduleMapper scheduleMapper;

}
