package com.maco.followthebeat.feature.base.dto;

import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
class ArtistDTO {
    private UUID id;
    private String name;
    private String imgUrl;
    private List<String> genres;
    private Instant createdAt;
    private Instant updatedAt;
}