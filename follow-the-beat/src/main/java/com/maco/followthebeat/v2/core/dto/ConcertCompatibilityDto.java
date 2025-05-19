package com.maco.followthebeat.v2.core.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record ConcertCompatibilityDto(
        UUID id,
        UUID userId,
        UUID concertId,
        Float compatibility
) {}
