package com.maco.followthebeat.service.spotify.interfaces;

import com.maco.followthebeat.entity.spotify.DbSpotifyArtist;
import java.util.List;
import java.util.Optional;

public interface SpotifyArtistService {
    Optional<DbSpotifyArtist> getArtist(String spotifyId);
    DbSpotifyArtist saveArtist(DbSpotifyArtist artist);
    DbSpotifyArtist saveOrGetExistingArtist(DbSpotifyArtist artist);
    List<DbSpotifyArtist> getArtists(List<String> spotifyIds);
    boolean deleteArtist(String spotifyId);
    void syncArtistData(String spotifyId);
    void syncMultipleArtists(List<String> spotifyIds);
} 