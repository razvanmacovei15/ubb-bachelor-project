package com.maco.followthebeat.v2.data.scrappers.untold.service;

import com.maco.followthebeat.v2.data.scrappers.untold.model.UntoldArtist;
import com.maco.followthebeat.v2.data.scrappers.untold.model.UntoldFestivalResponse;
import com.maco.followthebeat.v2.core.dto.ArtistDTO;
import com.maco.followthebeat.v2.core.dto.FestivalDTO;
import com.maco.followthebeat.v2.core.dto.ScheduleDTO;
import com.maco.followthebeat.v2.core.dto.StageDTO;
import com.maco.followthebeat.v2.data.adapter.untold.mapper.UntoldMapper;
import com.maco.followthebeat.v2.spotify.artists.entity.DbSpotifyArtist;
import com.maco.followthebeat.v2.spotify.artists.service.impl.SpotifyArtistServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UntoldServiceImpl implements UntoldService {

    private final UntoldMapper untoldMapper;
    private final SpotifyArtistServiceImpl spotifyArtistService;
    private LocalDate[] getDateRange(UntoldFestivalResponse untoldFestivalResponse) {
        String dateRange = untoldFestivalResponse.getDateRange();
        // Example input: "7 - 10 August 2025"
        String[] parts = dateRange.split(" ");
        int startDay = Integer.parseInt(parts[0]);
        int endDay = Integer.parseInt(parts[2]);
        String month = parts[3];
        int year = Integer.parseInt(parts[4]);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.ENGLISH);

        LocalDate startDate = LocalDate.parse(startDay + " " + month + " " + year, formatter);
        LocalDate endDate = LocalDate.parse(endDay + " " + month + " " + year, formatter);

        return new LocalDate[]{startDate, endDate};
    }

    @Override
    public FestivalDTO createFestival(UntoldFestivalResponse untoldFestivalResponse) {
        LocalDate[] dateRange = getDateRange(untoldFestivalResponse);

        return untoldMapper.mapFestivalDTO(untoldFestivalResponse, dateRange);
    }

    @Override
    public StageDTO createStage(UntoldArtist untoldArtist, FestivalDTO festivalDTO) {
        return StageDTO.builder()
                .name(untoldArtist.getStage())
                .festivalDTO(festivalDTO)
                .build();
    }

    @Override
    public ScheduleDTO createSchedule(UntoldArtist untoldArtist) {
        //todo implement this properly when there is a valid date format on the website
        String date = untoldArtist.getDate();
        String startTime = untoldArtist.getTime();
        LocalDate localDate = null;
        LocalTime localTime = null;
        if(date.equals("TO BE ANNOUNCED") || startTime.equals("TO BE ANNOUNCED")){
            return ScheduleDTO.builder()
                    .date(localDate)
                    .startTime(localTime)
                    .build();
        }
        return ScheduleDTO.builder()
                .date(null)
                .startTime(null)
                .build();
    }

    public void addGenresToArtistDTO(ArtistDTO artistDTO){
        Optional<DbSpotifyArtist> artist = spotifyArtistService.getArtistByName(artistDTO.getName());
        if(artist.isPresent()){
            artistDTO.setGenres(artist.get().getGenres());
        } else {
            //todo else implement search from spotify api for the artist to get more info maybe
            artistDTO.setGenres(Collections.emptyList());
        }
    }
}
