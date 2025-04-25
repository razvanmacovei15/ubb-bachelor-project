package com.maco.followthebeat.spotify.service;

import com.maco.followthebeat.entity.SpotifyUserData;
import com.maco.followthebeat.entity.User;
import com.maco.spotify.api.client.SpotifyClient;

import java.util.Optional;
import java.util.UUID;

public interface SpotifyDataService {
    void createSpotifyData(SpotifyUserData spotifyUserData);
    SpotifyUserData createSpotifyData(SpotifyClient spotifyClient, User user);
    void deleteSpotifyData(UUID userId);
    void updateSpotifyData(SpotifyUserData spotifyUserData);
    Optional<SpotifyUserData> getSpotifyData(UUID userId);
    Optional<SpotifyUserData> getSpotifyDataByUser(User user);
    boolean isSpotifyDataPresent(UUID userId);
}
