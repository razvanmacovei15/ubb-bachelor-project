package com.maco.followthebeat.v2.data.scrappers.electriccastle.service;

import com.maco.followthebeat.v2.core.dto.ArtistDTO;
import com.maco.followthebeat.v2.core.dto.FestivalDTO;
import com.maco.followthebeat.v2.core.dto.ScheduleDTO;
import com.maco.followthebeat.v2.core.dto.StageDTO;
import com.maco.followthebeat.v2.data.scrappers.electriccastle.model.ElectricArtist;
import com.maco.followthebeat.v2.data.scrappers.electriccastle.model.ElectricFestivalResponse;

import java.time.LocalDate;

public interface ElectricService {
    FestivalDTO createFestivalDTO(ElectricFestivalResponse electricFestivalResponse);
    StageDTO createStageDTO(ElectricArtist electricArtist, FestivalDTO festivalDTO);
    ScheduleDTO createScheduleDTO(ElectricArtist electricArtist, LocalDate date);
    ArtistDTO createArtistDTO(ElectricArtist electricArtist);
    void addGenresToArtistDTO(ArtistDTO artistDTO);
}
