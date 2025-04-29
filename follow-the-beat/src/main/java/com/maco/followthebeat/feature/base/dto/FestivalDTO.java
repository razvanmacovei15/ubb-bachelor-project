package com.maco.followthebeat.feature.base.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
@Data
@Builder
@RequiredArgsConstructor
public class FestivalDTO {
    private UUID id;
    private String name;
    private String description;
    private String location;
    private LocalDate startDate;
    private LocalDate endDate;
    private String logoUrl;
    private String websiteUrl;
    private Boolean isActive;
    private List<ArtistDTO> dtoArtists;
    private List<StageDTO> dtoStages;
}