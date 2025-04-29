package com.maco.followthebeat.feature.base.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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