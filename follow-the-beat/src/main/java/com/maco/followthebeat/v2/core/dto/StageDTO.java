package com.maco.followthebeat.v2.core.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StageDTO extends LocationDTO {
    private UUID id;
    private String name;
    private FestivalDTO festivalDTO;
    private List<ConcertDTO> dtoConcerts;
}