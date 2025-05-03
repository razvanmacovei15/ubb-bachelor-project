package com.maco.followthebeat.v2.data.scrappers.untold.api;

import com.maco.followthebeat.v2.data.scrappers.untold.model.UntoldArtist;
import com.maco.followthebeat.v2.data.scrappers.untold.model.UntoldFestivalResponse;
import com.maco.followthebeat.v2.core.dto.ArtistDTO;
import com.maco.followthebeat.v2.core.dto.FestivalDTO;
import com.maco.followthebeat.v2.core.dto.ScheduleDTO;
import com.maco.followthebeat.v2.core.dto.StageDTO;

import java.util.List;

public interface UntoldService {
    FestivalDTO createFestival(UntoldFestivalResponse untoldFestivalResponse);
    StageDTO createStage(UntoldArtist untoldArtist, FestivalDTO festivalDTO);
    ScheduleDTO createSchedule(UntoldArtist untoldArtist);
    List<ArtistDTO> getArtists(List<UntoldArtist> untoldArtists);
    void addGenresToArtistDTO(ArtistDTO artistDTO);
    String createConcertName(List<ArtistDTO> artistDTOs);
}
