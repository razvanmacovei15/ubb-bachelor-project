package com.maco.followthebeat.mapper;

import com.maco.client.v2.model.SpotifyArtist;
import com.maco.client.v2.model.extra.Image;
import com.maco.followthebeat.dto.spotify.SpotifyArtistDto;
import com.maco.followthebeat.entity.spotify.DbSpotifyArtist;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class SpotifyArtistMapper {
    
    public DbSpotifyArtist clientToEntity(SpotifyArtist clientArtist) {
        DbSpotifyArtist entity = new DbSpotifyArtist();
        entity.setSpotifyId(clientArtist.getId());
        entity.setName(clientArtist.getName());
        entity.setImageUrl(Arrays.stream(clientArtist.getImages()).findFirst().map(Image::getUrl).orElse(null));
        entity.setGenres(List.of(clientArtist.getGenres()));
        entity.setPopularity(clientArtist.getPopularity());
        return entity;
    }

    public SpotifyArtistDto entityToDto(DbSpotifyArtist entity) {
        SpotifyArtistDto dto = new SpotifyArtistDto();
        dto.setId(entity.getSpotifyId());
        dto.setName(entity.getName());
        dto.setImageUrl(entity.getImageUrl());
        dto.setGenres(entity.getGenres());
        dto.setPopularity(entity.getPopularity());
        return dto;
    }

    public SpotifyArtistDto clientToDto(SpotifyArtist clientArtist) {
        SpotifyArtistDto dto = new SpotifyArtistDto();
        dto.setId(clientArtist.getId());
        dto.setName(clientArtist.getName());
        dto.setImageUrl(Arrays.stream(clientArtist.getImages()).findFirst().map(Image::getUrl).orElse(null));
        dto.setGenres(List.of(clientArtist.getGenres()));
        dto.setPopularity(clientArtist.getPopularity());
        return dto;
    }
} 