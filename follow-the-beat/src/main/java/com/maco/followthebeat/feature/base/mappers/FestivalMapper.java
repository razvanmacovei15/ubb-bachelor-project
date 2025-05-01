package com.maco.followthebeat.feature.base.mappers;

import com.maco.followthebeat.feature.base.dto.FestivalDTO;
import com.maco.followthebeat.feature.base.entity.Festival;
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
}
