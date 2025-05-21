package com.maco.followthebeat.v2.spotify.artists.dto;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class SpotifyArtistDto {
    private String id;
    private String name;
    private String imageUrl;
    private List<String> genres;
    private Integer popularity;
    private Integer rank;
} 