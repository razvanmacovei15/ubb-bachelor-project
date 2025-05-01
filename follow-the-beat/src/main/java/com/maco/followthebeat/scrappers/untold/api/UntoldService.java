package com.maco.followthebeat.scrappers.untold.api;

import com.maco.followthebeat.feature.base.dto.*;

import java.util.List;

public interface UntoldService {
    FestivalDTO createFestival(UntoldFestivalResponse untoldFestivalResponse);
    StageDTO createStage(UntoldArtist untoldArtist, FestivalDTO festivalDTO);
    ScheduleDTO createSchedule(UntoldArtist untoldArtist);
    List<ArtistDTO> getArtists(List<UntoldArtist> untoldArtists);
    void addGenresToArtistDTO(ArtistDTO artistDTO);
    String createConcertName(List<ArtistDTO> artistDTOs);
}
