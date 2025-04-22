package com.maco.followthebeat.service.interfaces;

import com.maco.followthebeat.entity.SpotifyPlatform;

import java.util.UUID;

public interface SpotifyPlatformServiceI {
    void createSpotifyPlatform(SpotifyPlatform spotifyPlatform);
    void deleteSpotifyPlatform(UUID userId);
    void updateSpotifyPlatform(SpotifyPlatform spotifyPlatform);
    String getSpotifyPlatform(UUID userId);
}
