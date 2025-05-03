package com.maco.followthebeat.v2.data.scrappers.electriccastle.service;

import com.maco.followthebeat.v2.core.dto.ArtistDTO;
import com.maco.followthebeat.v2.core.dto.FestivalDTO;
import com.maco.followthebeat.v2.core.dto.ScheduleDTO;
import com.maco.followthebeat.v2.core.dto.StageDTO;
import com.maco.followthebeat.v2.data.adapter.untold.mapper.ElectricMapper;
import com.maco.followthebeat.v2.data.scrappers.electriccastle.model.ElectricArtist;
import com.maco.followthebeat.v2.data.scrappers.electriccastle.model.ElectricFestivalResponse;
import com.maco.followthebeat.v2.spotify.artists.entity.DbSpotifyArtist;
import com.maco.followthebeat.v2.spotify.artists.service.impl.SpotifyArtistServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ElectricServiceImpl implements ElectricService{
    private final ElectricMapper electricMapper;
    private final SpotifyArtistServiceImpl spotifyArtistService;


    @Override
    public FestivalDTO createFestivalDTO(ElectricFestivalResponse electricFestivalResponse) {
        return electricMapper.mapToFestivalDTO(electricFestivalResponse);
    }

    @Override
    public StageDTO createStageDTO(ElectricArtist electricArtist, FestivalDTO festivalDTO) {
        return electricMapper.mapToStageDTO(electricArtist, festivalDTO);
    }

    @Override
    public ScheduleDTO createScheduleDTO(ElectricArtist electricArtist, LocalDate date) {
        int day = Integer.parseInt(electricArtist.getDay());
        LocalDate performanceDate = date.plusDays(day - 1);
        return ScheduleDTO.builder()
                .date(performanceDate)
                .startTime(null)
                .build();
    }

    @Override
    public ArtistDTO createArtistDTO(ElectricArtist electricArtist) {
        return electricMapper.mapToArtistDTO(electricArtist);
    }


    @Override
    public void addGenresToArtistDTO(ArtistDTO artistDTO) {
        Optional<DbSpotifyArtist> artist = spotifyArtistService.getArtistByName(artistDTO.getName());
        if(artist.isPresent()){
            artistDTO.setGenres(artist.get().getGenres());
        } else {
            //todo else implement search from spotify api for the artist to get more info maybe
            artistDTO.setGenres(Collections.emptyList());
        }
    }
}
