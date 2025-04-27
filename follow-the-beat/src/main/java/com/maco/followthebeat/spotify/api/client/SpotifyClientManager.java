package com.maco.followthebeat.spotify.api.client;

import com.maco.client.v2.SpotifyClientI;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@Scope("singleton")
@AllArgsConstructor
public class SpotifyClientManager {
    private final Map<UUID, SpotifyClientI> userClients = new ConcurrentHashMap<>();
    private final SpotifyClientFactory clientFactory;

    public SpotifyClientI getOrCreateSpotifyClient(UUID userId) {
        if (userClients.containsKey(userId)) {
            return userClients.get(userId);
        }

        SpotifyClientI client = clientFactory.createSpotifyClient();

        userClients.put(userId, client);
        return client;
    }

    public void updateClientToken(UUID userId, SpotifyClientI client) {
        if (userClients.containsKey(userId)) {
            userClients.put(userId, client);
        }
    }

    public boolean isUserAuthenticated(UUID userId) {
        if (!userClients.containsKey(userId)) {
            return false;
        }
        SpotifyClientI client = userClients.get(userId);
        return client.isAuthenticated();
    }
}