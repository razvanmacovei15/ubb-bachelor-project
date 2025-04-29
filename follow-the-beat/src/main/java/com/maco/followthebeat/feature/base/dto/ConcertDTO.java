package com.maco.followthebeat.feature.base.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
@Data
@Builder
@RequiredArgsConstructor
public class ConcertDTO {
    private UUID id;
    private String name;
    private String description;
    private Instant createdAt;
    private Instant updatedAt;
    private LocationDTO locationDTO;
    private List<ArtistDTO> dtoArtists;
    private ScheduleDTO scheduleDTO;
}
