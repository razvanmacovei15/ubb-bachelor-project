package com.maco.followthebeat.v2.core.dto;

import lombok.*;

import java.util.List;

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