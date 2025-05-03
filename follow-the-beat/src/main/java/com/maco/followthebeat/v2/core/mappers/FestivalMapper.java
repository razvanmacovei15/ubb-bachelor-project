package com.maco.followthebeat.v2.core.mappers;

import com.maco.followthebeat.v2.core.dto.FestivalDTO;
import com.maco.followthebeat.v2.core.entity.Festival;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class FestivalMapper {
    public Festival toEntity(FestivalDTO festivalDTO) {
        return Festival.builder()
                .id(festivalDTO.getId())
                .name(festivalDTO.getName())
                .description(festivalDTO.getDescription())
                .location(festivalDTO.getLocation())
                .startDate(festivalDTO.getStartDate())
                .endDate(festivalDTO.getEndDate())
                .logoUrl(festivalDTO.getLogoUrl())
                .websiteUrl(festivalDTO.getWebsiteUrl())
                .isActive(festivalDTO.getIsActive())
                .stages(new HashSet<>())
                .build();
    }

    public FestivalDTO toDTO(Festival festival) {
        return FestivalDTO.builder()
                .id(festival.getId())
                .name(festival.getName())
                .description(festival.getDescription())
                .location(festival.getLocation())
                .startDate(festival.getStartDate())
                .endDate(festival.getEndDate())
                .logoUrl(festival.getLogoUrl())
                .websiteUrl(festival.getWebsiteUrl())
                .isActive(festival.getIsActive())
                .dtoStages(new HashSet<>())
                .build();
    }
}
