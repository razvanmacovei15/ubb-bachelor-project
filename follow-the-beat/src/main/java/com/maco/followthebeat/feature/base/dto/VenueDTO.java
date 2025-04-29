package com.maco.followthebeat.feature.base.dto;

import lombok.*;

import java.util.List;
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VenueDTO extends LocationDTO {
    private String address;
    private String city;
    private String country;
    private Integer capacity;
    private List<ConcertDTO> dtoConcerts;
}