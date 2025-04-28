package com.maco.followthebeat.v2.spotify.tracks.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ImageDto {
    private String url;
    private int height;
    private int width;
}
