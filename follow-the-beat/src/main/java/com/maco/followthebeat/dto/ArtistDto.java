package com.maco.followthebeat.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ArtistDto {
    private UUID id;
    private String name;
    private String imageKey;
    private String imageUrl;
    private List<String> genres;
} 