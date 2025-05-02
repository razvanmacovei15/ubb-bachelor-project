package com.maco.followthebeat.v2.core.dto;

import lombok.*;

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
