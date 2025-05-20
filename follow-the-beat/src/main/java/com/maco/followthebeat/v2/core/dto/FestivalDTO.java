package com.maco.followthebeat.v2.core.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
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
    private Set<StageDTO> dtoStages;
    private String festivalImageUrl;
}