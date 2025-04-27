package com.maco.followthebeat.v1.spotify.interfaces;

import com.maco.followthebeat.v1.spotify.DbSpotifyTrack;

import java.util.List;
import java.util.Optional;

public interface SpotifyTrackService {
    Optional<DbSpotifyTrack> getTrack(String spotifyId);
    DbSpotifyTrack saveTrack(DbSpotifyTrack track);
    List<DbSpotifyTrack> getTracks(List<String> spotifyIds);
    boolean deleteTrack(String spotifyId);
    void syncTrackData(String spotifyId);
    void syncMultipleTracks(List<String> spotifyIds);
} 