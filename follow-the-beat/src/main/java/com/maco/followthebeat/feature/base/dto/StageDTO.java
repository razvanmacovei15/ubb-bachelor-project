package com.maco.followthebeat.feature.base.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StageDTO extends LocationDTO {
    private String name;
    private FestivalDTO festivalDTO;
    private List<ConcertDTO> dtoConcerts;
}