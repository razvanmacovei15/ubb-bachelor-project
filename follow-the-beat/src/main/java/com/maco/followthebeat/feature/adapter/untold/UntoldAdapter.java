package com.maco.followthebeat.feature.adapter.untold;

import com.maco.followthebeat.feature.base.dto.*;
import com.maco.followthebeat.feature.base.entity.*;
import com.maco.followthebeat.feature.base.service.impl.SuperService;
import com.maco.followthebeat.scrappers.untold.api.UntoldArtist;
import com.maco.followthebeat.scrappers.untold.api.UntoldFestivalResponse;
import com.maco.followthebeat.scrappers.untold.api.UntoldMapper;
import com.maco.followthebeat.scrappers.untold.api.UntoldService;
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
        log.info("🔄 Starting festival save flow...");

        FestivalDTO festivalDTO = untoldService.createFestival(untoldFestivalResponse);
        Festival festival = festivalService.save(festivalMapper.toEntity(festivalDTO));
        log.info("✅ Saved Festival: {}", festival.getName());

        List<UntoldArtist> untoldArtists = untoldFestivalResponse.getArtists();
        for(UntoldArtist untoldArtist : untoldArtists){
            log.info("🎤 Processing artist: {}", untoldArtist.getName());

            StageDTO stageDTO = untoldService.createStage(untoldArtist, festivalDTO);

            Optional<Stage> existingStageOpt = stageService.findByNameAndFestivalId(stageDTO.getName(), festival.getId());

            Stage stage = existingStageOpt.orElseGet(() -> {
                Stage newStage = stageMapper.toEntity(stageDTO);
                newStage.setFestival(festival);
                return stageService.save(newStage);git
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

            log.info("🎶 Saved concert for artist: {}", artist.getName());
        }
    }

}

