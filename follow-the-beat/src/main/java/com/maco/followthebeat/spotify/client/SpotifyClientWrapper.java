package com.maco.followthebeat.spotify.client;

import com.maco.followthebeat.entity.SpotifyUserData;
import com.maco.followthebeat.spotify.repository.SpotifyDataRepo;
import com.maco.followthebeat.spotify.service.SpotifyDataService;
import com.maco.spotify.api.client.SpotifyClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SpotifyClientWrapper {
    private final SpotifyClientFactory clientFactory;
    private final SpotifyDataService spotifyDataService;

    public SpotifyClient getClientForUser(UUID userId) {
        SpotifyUserData platform = spotifyDataService.getSpotifyData(userId)
                .orElseThrow(() -> new RuntimeException("User not connected to Spotify"));

        return clientFactory.createSpotifyClient();
    }
}