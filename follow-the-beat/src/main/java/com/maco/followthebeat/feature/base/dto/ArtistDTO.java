package com.maco.followthebeat.feature.base.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
@Data
@Builder
@RequiredArgsConstructor
public class ArtistDTO {
    private UUID id;
    private String name;
    private String imgUrl;
    private List<String> genres;
    private Instant createdAt;
    private Instant updatedAt;
}