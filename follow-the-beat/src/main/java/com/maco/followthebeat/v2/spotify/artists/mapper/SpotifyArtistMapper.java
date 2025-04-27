package com.maco.followthebeat.v2.spotify.artists.mapper;

import com.maco.client.v2.model.SpotifyArtist;
import com.maco.client.v2.model.extra.Image;
import com.maco.followthebeat.v2.spotify.artists.dto.SpotifyArtistDto;
import com.maco.followthebeat.v2.spotify.artists.entity.BaseUserTopArtist;
import com.maco.followthebeat.v2.spotify.artists.entity.DbSpotifyArtist;
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

    public SpotifyArtistDto clientToDto(SpotifyArtist clientArtist) {
        SpotifyArtistDto dto = new SpotifyArtistDto();
        dto.setId(clientArtist.getId());
        dto.setName(clientArtist.getName());
        dto.setImageUrl(Arrays.stream(clientArtist.getImages()).findFirst().map(Image::getUrl).orElse(null));
        dto.setGenres(List.of(clientArtist.getGenres()));
        dto.setPopularity(clientArtist.getPopularity());
        return dto;
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

    public DbSpotifyArtist dtoToEntity(SpotifyArtistDto dto) {
        DbSpotifyArtist entity = new DbSpotifyArtist();
        entity.setSpotifyId(dto.getId());
        entity.setName(dto.getName());
        entity.setImageUrl(dto.getImageUrl());
        entity.setGenres(dto.getGenres());
        entity.setPopularity(dto.getPopularity());
        return entity;
    }

    public SpotifyArtistDto mapSpecificEntityToDto(BaseUserTopArtist artist) {
        SpotifyArtistDto dto = new SpotifyArtistDto();
        dto.setId(artist.getArtist().getSpotifyId());
        dto.setName(artist.getArtist().getName());
        dto.setPopularity(artist.getArtist().getPopularity());
        dto.setImageUrl(artist.getArtist().getImageUrl());
        dto.setGenres(artist.getArtist().getGenres());
        return dto;
    }


} 