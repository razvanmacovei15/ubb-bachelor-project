package com.maco.followthebeat.v2.core.mappers;

import com.maco.followthebeat.v2.core.dto.ArtistDTO;
import com.maco.followthebeat.v2.core.dto.ArtistForSuggestionDto;
import com.maco.followthebeat.v2.core.entity.Artist;
import org.springframework.stereotype.Component;

@Component

public class ArtistMapper {
    public Artist toEntity(ArtistDTO artistDTO) {
        return Artist.builder()
                .name(artistDTO.getName())
                .imgUrl(artistDTO.getImgUrl())
                .build();
    }

    public ArtistDTO toDTO(Artist artist) {
        return ArtistDTO.builder()
                .id(artist.getId())
                .name(artist.getName())
                .imgUrl(artist.getImgUrl())
                .genres(artist.getGenres())
                .build();
    }

    public ArtistForSuggestionDto toSuggestionDto(Artist artist) {
        return ArtistForSuggestionDto.builder()
                .name(artist.getName())
                .genres(artist.getGenres())
                .build();
    }
}
