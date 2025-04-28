package com.maco.followthebeat.v2.spotify.tracks.mapper;

import com.maco.client.v2.model.SpotifyTrack;
import com.maco.client.v2.model.extra.Image;
import com.maco.followthebeat.v2.spotify.tracks.dto.SpotifyTrackDto;
import com.maco.followthebeat.v2.spotify.tracks.dto.TrackArtistDto;
import com.maco.followthebeat.v2.spotify.tracks.entity.BaseUserTopTrack;
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
                .albumImgUrl(extractFirstAlbumImageUrl(track))
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

    public SpotifyTrackDto fromDbSpotifyTrack(BaseUserTopTrack dbTrack) {
        return SpotifyTrackDto.builder()
                .spotifyId(dbTrack.getTrack().getSpotifyId())
                .name(dbTrack.getTrack().getName())
                .durationMs(dbTrack.getTrack().getDurationMs())
                .popularity(dbTrack.getTrack().getPopularity())
                .previewUrl(dbTrack.getTrack().getPreviewUrl())
                .albumImgUrl(dbTrack.getTrack().getAlbumImgUrl())
                .artists(dbTrack.getTrack().getArtists() != null ?
                        dbTrack.getTrack().getArtists().stream()
                                .map(this::fromTrackArtist)
                                .collect(Collectors.toList())
                        : List.of())
                .rank(dbTrack.getRank())
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
        dbTrack.setAlbumImgUrl(dto.getAlbumImgUrl());
        return dbTrack;
    }

    // Helper method to extract the first album image URL
    private String extractFirstAlbumImageUrl(SpotifyTrack track) {
        if (track.getAlbum() != null && track.getAlbum().getImages() != null && track.getAlbum().getImages().length > 0) {
            return track.getAlbum().getImages()[0].getUrl();
        }
        return null;
    }
}
