package com.maco.followthebeat.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class TrackDto {
    private UUID id;
    private String name;
    private ArtistDto artist;
    private String album;
    private String imageKey;
    private String imageUrl;
    private List<String> genres;
} 