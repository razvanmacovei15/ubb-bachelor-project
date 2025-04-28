package com.maco.followthebeat.v2.spotify.tracks.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AlbumDto {
    private String name;
    private ImageDto[] images;
}
