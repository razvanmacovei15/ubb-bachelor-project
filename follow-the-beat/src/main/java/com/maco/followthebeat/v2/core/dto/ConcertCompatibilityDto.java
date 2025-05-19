package com.maco.followthebeat.v2.core.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;


@Data
@Builder
public class ConcertCompatibilityDto {
    private UUID id;
    private UUID userId;
    private UUID concertId;
    private Float compatibility;
}


