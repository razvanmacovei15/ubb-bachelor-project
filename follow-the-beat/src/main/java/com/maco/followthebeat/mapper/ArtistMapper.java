package com.maco.followthebeat.mapper;

import com.maco.followthebeat.dto.ArtistDto;
import com.maco.followthebeat.entity.Artist;
import com.maco.followthebeat.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArtistMapper {
    private final S3Service s3Service;

    public ArtistDto toDto(Artist artist) {
        ArtistDto dto = new ArtistDto();
        dto.setId(artist.getId());
        dto.setName(artist.getName());
        dto.setImageKey(artist.getImageKey());
        dto.setGenres(artist.getGenres());
        
        // Generate preSigned URL if image key exists
        if (artist.getImageKey() != null) {
            try {
                dto.setImageUrl(s3Service.generatePresignedUrl(artist.getImageKey()));
            } catch (Exception e) {
                // Log error but don't fail the request
                // The frontend will handle missing image URLs gracefully
            }
        }
        
        return dto;
    }
} 