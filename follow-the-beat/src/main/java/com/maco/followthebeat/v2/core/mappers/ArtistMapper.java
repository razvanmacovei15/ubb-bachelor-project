package com.maco.followthebeat.v2.core.mappers;

import com.maco.followthebeat.v2.core.dto.ArtistDTO;
import com.maco.followthebeat.v2.core.dto.ConcertForSuggestionDto;
import com.maco.followthebeat.v2.core.entity.Artist;
import com.maco.followthebeat.v2.core.entity.Concert;
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


}
