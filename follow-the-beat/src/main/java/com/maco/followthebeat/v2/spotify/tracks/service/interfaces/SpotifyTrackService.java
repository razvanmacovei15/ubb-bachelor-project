package com.maco.followthebeat.v2.spotify.tracks.service.interfaces;

import com.maco.followthebeat.v2.spotify.tracks.entity.DbSpotifyTrack;

import java.util.List;
import java.util.Optional;

public interface SpotifyTrackService {
    Optional<DbSpotifyTrack> getTrack(String spotifyId);
    DbSpotifyTrack saveTrack(DbSpotifyTrack track);
    DbSpotifyTrack saveOrGetExistingTrack(DbSpotifyTrack track);
    List<DbSpotifyTrack> getTracks(List<String> spotifyIds);
    boolean deleteTrack(String spotifyId);
    void syncTrackData(String spotifyId);
    void syncMultipleTracks(List<String> spotifyIds);
}