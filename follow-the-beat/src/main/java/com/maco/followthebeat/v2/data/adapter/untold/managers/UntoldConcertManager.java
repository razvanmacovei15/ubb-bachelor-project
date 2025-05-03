package com.maco.followthebeat.v2.data.adapter.untold.managers;

import com.maco.followthebeat.v2.core.dto.ArtistDTO;
import com.maco.followthebeat.v2.core.dto.FestivalDTO;
import com.maco.followthebeat.v2.core.dto.ScheduleDTO;
import com.maco.followthebeat.v2.core.dto.StageDTO;
import com.maco.followthebeat.v2.core.entity.*;
import com.maco.followthebeat.v2.core.mappers.ArtistMapper;
import com.maco.followthebeat.v2.core.mappers.ScheduleMapper;
import com.maco.followthebeat.v2.core.mappers.StageMapper;
import com.maco.followthebeat.v2.core.service.interfaces.ArtistService;
import com.maco.followthebeat.v2.core.service.interfaces.ConcertService;
import com.maco.followthebeat.v2.core.service.interfaces.ScheduleService;
import com.maco.followthebeat.v2.core.service.interfaces.StageService;
import com.maco.followthebeat.v2.data.adapter.untold.mapper.UntoldMapper;
import com.maco.followthebeat.v2.data.scrappers.untold.service.UntoldService;
import com.maco.followthebeat.v2.data.scrappers.untold.model.UntoldArtist;
import com.maco.followthebeat.v2.data.scrappers.untold.model.UntoldFestivalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UntoldConcertManager {

    private final ConcertService concertService;
    private final StageService stageService;
    private final ArtistService artistService;
    private final ScheduleService scheduleService;
    private final UntoldService untoldService;
    private final UntoldMapper untoldMapper;
    private final StageMapper stageMapper;
    private final ArtistMapper artistMapper;
    private final ScheduleMapper scheduleMapper;

    public void replaceConcertsForFestival(Festival festival, UntoldFestivalResponse response) {
        concertService.deleteByFestivalId(festival.getId());

        FestivalDTO festivalDTO = untoldService.createFestival(response);

        for (UntoldArtist artistRaw : response.getArtists()) {
            Stage stage = getOrCreateStage(artistRaw, festival, festivalDTO);
            Artist artist = getOrCreateArtist(artistRaw);
            Schedule schedule = createSchedule(artistRaw);

            Concert concert = Concert.builder()
                    .location(stage)
                    .artist(artist)
                    .schedule(schedule)
                    .build();

            concertService.save(concert);
        }
    }

    private Stage getOrCreateStage(UntoldArtist raw, Festival fest, FestivalDTO dto) {
        StageDTO stageDTO = untoldService.createStage(raw, dto);
        return stageService.findByNameAndFestivalId(stageDTO.getName(), fest.getId())
                .orElseGet(() -> {
                    Stage stage = stageMapper.toEntity(stageDTO);
                    stage.setFestival(fest);
                    return stageService.save(stage);
                });
    }

    private Artist getOrCreateArtist(UntoldArtist raw) {
        ArtistDTO dto = untoldMapper.mapArtistDTO(raw);
        untoldService.addGenresToArtistDTO(dto);

        return artistService.findByName(dto.getName())
                .orElseGet(() -> artistService.save(artistMapper.toEntity(dto)));
    }

    private Schedule createSchedule(UntoldArtist raw) {
        ScheduleDTO dto = untoldService.createSchedule(raw);
        return scheduleService.save(scheduleMapper.toEntity(dto));
    }
}