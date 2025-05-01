package com.maco.followthebeat.v2.spotify.artists.service.interfaces;

import com.maco.followthebeat.v2.spotify.artists.entity.DbSpotifyArtist;
import java.util.List;
import java.util.Optional;

public interface SpotifyArtistService {
    Optional<DbSpotifyArtist> getArtist(String spotifyId);
    Optional<DbSpotifyArtist> getArtistByName(String name);
    DbSpotifyArtist saveArtist(DbSpotifyArtist artist);
    DbSpotifyArtist saveOrGetExistingArtist(DbSpotifyArtist artist);
    List<DbSpotifyArtist> getArtists(List<String> spotifyIds);
    boolean deleteArtist(String spotifyId);
    void syncArtistData(String spotifyId);
    void syncMultipleArtists(List<String> spotifyIds);
} 