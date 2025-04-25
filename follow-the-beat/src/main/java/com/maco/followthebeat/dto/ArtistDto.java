package com.maco.followthebeat.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ArtistDto {
    private UUID id;
    private String name;
    private String imageKey;  // S3 object key
    private String imageUrl;  // Presigned URL (generated at runtime)
    private List<String> genres;
} 