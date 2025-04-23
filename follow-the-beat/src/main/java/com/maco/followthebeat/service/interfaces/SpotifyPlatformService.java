package com.maco.followthebeat.service.interfaces;

import com.maco.followthebeat.entity.SpotifyPlatform;
import com.maco.followthebeat.entity.User;
import com.maco.spotify.api.client.SpotifyClient;

import java.util.Optional;
import java.util.UUID;

public interface SpotifyPlatformService {
    void createSpotifyPlatform(SpotifyPlatform spotifyPlatform);
    SpotifyPlatform createSpotifyPlatform(SpotifyClient spotifyClient, User user);
    void deleteSpotifyPlatform(UUID userId);
    void updateSpotifyPlatform(SpotifyPlatform spotifyPlatform);
    Optional<SpotifyPlatform> getSpotifyPlatform(UUID userId);
    Optional<SpotifyPlatform> getSpotifyPlatformByUser(User user);
}
