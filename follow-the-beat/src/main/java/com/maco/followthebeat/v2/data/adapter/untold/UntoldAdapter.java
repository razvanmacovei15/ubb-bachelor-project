package com.maco.followthebeat.v2.data.adapter.untold;

import com.maco.followthebeat.v2.core.dto.ArtistDTO;
import com.maco.followthebeat.v2.core.dto.FestivalDTO;
import com.maco.followthebeat.v2.core.dto.ScheduleDTO;
import com.maco.followthebeat.v2.core.dto.StageDTO;
import com.maco.followthebeat.v2.core.entity.*;
import com.maco.followthebeat.v2.core.service.impl.SuperService;
import com.maco.followthebeat.v2.data.scrappers.untold.model.UntoldArtist;
import com.maco.followthebeat.v2.data.scrappers.untold.model.UntoldFestivalResponse;
import com.maco.followthebeat.v2.data.adapter.mapper.UntoldMapper;
import com.maco.followthebeat.v2.data.scrappers.untold.api.UntoldService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
@Slf4j
@Component
@RequiredArgsConstructor
public class UntoldAdapter extends SuperService {
    private final UntoldService untoldService;
    private final UntoldMapper untoldMapper;

    @Transactional
    public void saveUntoldFestival(UntoldFestivalResponse untoldFestivalResponse){

        FestivalDTO festivalDTO = untoldService.createFestival(untoldFestivalResponse);
        Festival festival = festivalService.save(festivalMapper.toEntity(festivalDTO));

        List<UntoldArtist> untoldArtists = untoldFestivalResponse.getArtists();
        for(UntoldArtist untoldArtist : untoldArtists){

            StageDTO stageDTO = untoldService.createStage(untoldArtist, festivalDTO);

            Optional<Stage> existingStageOpt = stageService.findByNameAndFestivalId(stageDTO.getName(), festival.getId());
            Stage stage = existingStageOpt.orElseGet(() -> {
                Stage newStage = stageMapper.toEntity(stageDTO);
                newStage.setFestival(festival);
                return stageService.save(newStage);
            });

            ArtistDTO artistDTO = untoldMapper.mapArtistDTO(untoldArtist);
            untoldService.addGenresToArtistDTO(artistDTO);
            Optional<Artist> optionalArtist = artistService.findByName(artistDTO.getName());
            Artist artist = optionalArtist.orElseGet(() -> artistService.save(artistMapper.toEntity(artistDTO)));

            ScheduleDTO scheduleDTO = untoldService.createSchedule(untoldArtist);
            Schedule schedule = scheduleService.save(scheduleMapper.toEntity(scheduleDTO));

            Concert concert = Concert.builder()
                    .location(stage)
                    .artist(artist)
                    .schedule(schedule)
                    .build();
            concertService.save(concert);
        }
    }

}

