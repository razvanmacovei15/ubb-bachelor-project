package com.maco.followthebeat.feature.base.dto;

import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConcertDTO {
    private UUID id;
    private LocationDTO locationDTO;
    private ArtistDTO artistDTO;
    private ScheduleDTO scheduleDTO;
}
