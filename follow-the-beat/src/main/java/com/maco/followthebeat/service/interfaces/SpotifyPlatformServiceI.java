package com.maco.followthebeat.service.interfaces;

import java.util.UUID;

public interface SpotifyPlatformServiceI {
    void createSpotifyPlatform(UUID userId, String accessToken, String refreshToken, String tokenType, String scope, long expiresIn);
    void deleteSpotifyPlatform(UUID userId);
    void updateSpotifyPlatform(UUID userId, String accessToken, String refreshToken, String tokenType, String scope, long expiresIn);
    String getSpotifyPlatform(UUID userId);
}
