package com.maco.followthebeat.feature.base.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@RequiredArgsConstructor
public class StageDTO extends LocationDTO {
    private String description;
    private FestivalDTO festivalDTO;
    private List<ConcertDTO> dtoConcerts;
}