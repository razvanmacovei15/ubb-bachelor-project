package com.maco.followthebeat.v2.core.mappers;

import com.maco.followthebeat.v2.core.dto.ArtistDTO;
import com.maco.followthebeat.v2.core.dto.ConcertDTO;
import com.maco.followthebeat.v2.core.dto.ScheduleDTO;
import com.maco.followthebeat.v2.core.dto.StageDTO;
import com.maco.followthebeat.v2.core.entity.Concert;
import com.maco.followthebeat.v2.core.entity.Stage;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
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


}
