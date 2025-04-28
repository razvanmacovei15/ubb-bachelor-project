package com.maco.followthebeat.v2.spotify.tracks.mapper;

import com.maco.client.v2.model.SpotifyTrack;
import com.maco.followthebeat.v2.spotify.tracks.dto.AlbumDto;
import com.maco.followthebeat.v2.spotify.tracks.dto.SpotifyTrackDto;
import com.maco.followthebeat.v2.spotify.tracks.dto.TrackArtistDto;
import com.maco.followthebeat.v2.spotify.tracks.entity.DbSpotifyTrack;
import com.maco.followthebeat.v2.spotify.tracks.entity.TrackArtist;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class SpotifyTrackMapper {

    public SpotifyTrackDto fromSpotifyTrack(SpotifyTrack track) {
        return SpotifyTrackDto.builder()
                .spotifyId(track.getId())
                .name(track.getName())
                .durationMs(track.getDurationMs())
                .popularity(track.getPopularity())
                .previewUrl(track.getPreviewUrl())
                .explicit(track.isExplicit())
                .album(AlbumDto.builder()
                        .name(track.getAlbum() != null ? track.getAlbum().getName() : null)
                        .imageUrl(track.getAlbum() != null && track.getAlbum().getImages() != null && track.getAlbum().getImages().length > 0
                                ? track.getAlbum().getImages()[0].getUrl()
                                : null)
                        .build())
                .artists(track.getSpotifyArtists() != null ?
                        Stream.of(track.getSpotifyArtists())
                                .map(artist -> TrackArtistDto.builder()
                                        .spotifyArtistId(artist.getId())
                                        .name(artist.getName())
                                        .build())
                                .collect(Collectors.toList())
                        : List.of())
                .build();
    }

    public SpotifyTrackDto fromDbSpotifyTrack(DbSpotifyTrack dbTrack) {
        return SpotifyTrackDto.builder()
                .spotifyId(dbTrack.getSpotifyId())
                .name(dbTrack.getName())
                .durationMs(dbTrack.getDurationMs())
                .popularity(dbTrack.getPopularity())
                .previewUrl(dbTrack.getPreviewUrl())
                .album(null)
                .artists(dbTrack.getArtists() != null ?
                        dbTrack.getArtists().stream()
                                .map(this::fromTrackArtist)
                                .collect(Collectors.toList())
                        : List.of())
                .build();
    }

    private TrackArtistDto fromTrackArtist(TrackArtist trackArtist) {
        return TrackArtistDto.builder()
                .spotifyArtistId(trackArtist.getArtist().getSpotifyId())
                .name(trackArtist.getArtist().getName())
                .build();
    }

    public DbSpotifyTrack toDbSpotifyTrack(SpotifyTrackDto dto) {
        DbSpotifyTrack dbTrack = new DbSpotifyTrack();
        dbTrack.setSpotifyId(dto.getSpotifyId());
        dbTrack.setName(dto.getName());
        dbTrack.setDurationMs(dto.getDurationMs());
        dbTrack.setPopularity(dto.getPopularity());
        dbTrack.setPreviewUrl(dto.getPreviewUrl());
        return dbTrack;
    }
}
