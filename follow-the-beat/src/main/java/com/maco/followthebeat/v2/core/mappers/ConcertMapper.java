package com.maco.followthebeat.v2.core.mappers;

import com.maco.followthebeat.v2.core.dto.*;
import com.maco.followthebeat.v2.core.entity.Concert;
import com.maco.followthebeat.v2.core.entity.Stage;
import com.maco.followthebeat.v2.core.model.ConcertResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class ConcertMapper {
    public ConcertDTO fromEntity(Concert concert) {
        return ConcertDTO.builder()
                .id(concert.getId())
                .artistDTO(ArtistDTO.builder()
                        .name(concert.getArtist().getName())
                        .imgUrl(concert.getArtist().getImgUrl())
                        .build())
                .locationDTO(StageDTO.builder()
                        .name(concert.getLocation().getName())
                        .build())
                .scheduleDTO(ScheduleDTO.builder()
                        .date(concert.getSchedule() != null ? concert.getSchedule().getDate() : null)
                        .startTime(concert.getSchedule() != null ? concert.getSchedule().getStartTime() : null)
                        .build())
                .build();
    }

    public ConcertResponseDto fromEntityToResponseDto(Concert concert) {
        String festivalName = null;

        if (concert.getLocation() instanceof Stage stage) {
            if (stage.getFestival() != null) {
                festivalName = stage.getFestival().getName();
            }
        }

        return ConcertResponseDto.builder()
                .concertId(concert.getId())
                .artistName(concert.getArtist().getName())
                .artistImageUrl(concert.getArtist().getImgUrl())
                .festivalName(festivalName)
                .stageName(concert.getLocation().getName())
                .startTime(concert.getSchedule() != null ? concert.getSchedule().getStartTime() : null)
                .date(concert.getSchedule() != null ? concert.getSchedule().getDate() : null)
                .build();
    }

    public ConcertForSuggestionDto toSuggestionDto(Concert concert) {
        if (concert.getId() == null) {
            log.warn("Attempted to map a concert with null ID: {}", concert);
            return null;
        }

        return ConcertForSuggestionDto.builder()
                .concertId(concert.getId())
                .artistName(concert.getArtist().getName())
                .genres(concert.getArtist().getGenres())
                .build();
    }
}
