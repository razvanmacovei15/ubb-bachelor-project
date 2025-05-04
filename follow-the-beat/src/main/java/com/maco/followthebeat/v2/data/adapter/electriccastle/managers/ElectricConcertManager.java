package com.maco.followthebeat.v2.data.adapter.electriccastle.managers;

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
import com.maco.followthebeat.v2.data.adapter.electriccastle.mapper.ElectricMapper;
import com.maco.followthebeat.v2.data.adapter.untold.mapper.UntoldMapper;
import com.maco.followthebeat.v2.data.scrappers.electriccastle.model.ElectricArtist;
import com.maco.followthebeat.v2.data.scrappers.electriccastle.model.ElectricFestivalResponse;
import com.maco.followthebeat.v2.data.scrappers.electriccastle.service.ElectricService;
import com.maco.followthebeat.v2.data.scrappers.untold.service.UntoldService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class ElectricConcertManager {
    private final ConcertService concertService;
    private final StageService stageService;
    private final ArtistService artistService;
    private final ScheduleService scheduleService;
    private final ElectricService electricService;
    private final ElectricMapper electricMapper;
    private final StageMapper stageMapper;
    private final ArtistMapper artistMapper;
    private final ScheduleMapper scheduleMapper;


    public void replaceConcertsForFestival(Festival festival, ElectricFestivalResponse electricFestivalResponse){
        concertService.deleteByFestivalId(festival.getId());

        FestivalDTO festivalDTO = electricService.createFestivalDTO(electricFestivalResponse);
        for(ElectricArtist electricArtist : electricFestivalResponse.getArtists()){
            Stage stage = getOrCreateStage(electricArtist, festival, festivalDTO);
            Artist artist = getOrCreateArtist(electricArtist);
            Schedule schedule = createSchedule(electricArtist, festival.getStartDate());

            Concert concert = Concert.builder()
                    .location(stage)
                    .artist(artist)
                    .schedule(schedule)
                    .build();
            concertService.save(concert);
        }

    }
    private Schedule createSchedule(ElectricArtist electricArtist, LocalDate localDate){
        ScheduleDTO dto = electricService.createScheduleDTO(electricArtist, localDate);
        return scheduleService.save(scheduleMapper.toEntity(dto));
    }

    private Artist getOrCreateArtist(ElectricArtist electricArtist){
        ArtistDTO dto = electricMapper.mapToArtistDTO(electricArtist);
        electricService.addGenresToArtistDTO(dto);

        return artistService.findByName(dto.getName())
                .orElseGet(() -> artistService.save(artistMapper.toEntity(dto)));
    }

    private Stage getOrCreateStage(ElectricArtist electricArtist, Festival festival, FestivalDTO festivalDTO){
        StageDTO stageDTO = electricService.createStageDTO(electricArtist, festivalDTO);
        return stageService.findByNameAndFestivalId(stageDTO.getName(), festival.getId())
                .orElseGet(() -> {
                    Stage stage = stageMapper.toEntity(stageDTO);
                    stage.setFestival(festival);
                    return stageService.save(stage);
                });
    }

}
