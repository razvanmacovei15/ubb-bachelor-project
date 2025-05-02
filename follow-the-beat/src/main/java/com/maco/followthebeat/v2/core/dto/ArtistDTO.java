package com.maco.followthebeat.v2.core.dto;

import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArtistDTO {
    private UUID id;
    private String name;
    private String imgUrl;
    private List<String> genres;
}