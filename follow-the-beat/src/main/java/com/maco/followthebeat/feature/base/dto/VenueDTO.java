package com.maco.followthebeat.feature.base.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.util.List;
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@RequiredArgsConstructor
public class VenueDTO extends LocationDTO {
    private String address;
    private String city;
    private String country;
    private Integer capacity;
    private List<ConcertDTO> dtoConcerts;
}