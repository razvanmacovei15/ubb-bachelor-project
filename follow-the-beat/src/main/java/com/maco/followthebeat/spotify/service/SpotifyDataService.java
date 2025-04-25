package com.maco.followthebeat.service.interfaces;

import com.maco.followthebeat.entity.SpotifyData;
import com.maco.followthebeat.entity.User;
import com.maco.spotify.api.client.SpotifyClient;

import java.util.Optional;
import java.util.UUID;

public interface SpotifyPlatformService {
    void createSpotifyPlatform(SpotifyData spotifyData);
    SpotifyData createSpotifyPlatform(SpotifyClient spotifyClient, User user);
    void deleteSpotifyPlatform(UUID userId);
    void updateSpotifyPlatform(SpotifyData spotifyData);
    Optional<SpotifyData> getSpotifyPlatform(UUID userId);
    Optional<SpotifyData> getSpotifyPlatformByUser(User user);
}
