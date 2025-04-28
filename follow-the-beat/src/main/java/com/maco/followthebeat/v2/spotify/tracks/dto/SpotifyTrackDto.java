package com.maco.followthebeat.v2.spotify.tracks.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SpotifyTrackDto {
    private String spotifyId;
    private String name;
    private Long durationMs;
    private Integer popularity;
    private String previewUrl;
    private boolean explicit;
    private AlbumDto album;
    private List<TrackArtistDto> artists;
}
