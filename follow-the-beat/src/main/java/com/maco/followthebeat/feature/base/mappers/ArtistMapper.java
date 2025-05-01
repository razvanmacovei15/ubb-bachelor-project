package com.maco.followthebeat.feature.base.mappers;

import com.maco.followthebeat.feature.base.dto.ArtistDTO;
import com.maco.followthebeat.feature.base.entity.Artist;
import org.springframework.stereotype.Component;

@Component

public class ArtistMapper {
    public Artist toEntity(ArtistDTO artistDTO) {
        return Artist.builder()
                .name(artistDTO.getName())
                .imgUrl(artistDTO.getImgUrl())
                .build();
    }
}
