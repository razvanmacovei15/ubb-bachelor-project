package com.maco.followthebeat.spotify.client;

import com.maco.followthebeat.spotify.client.SpotifyClientFactory;
import com.maco.followthebeat.spotify.service.SpotifyDataService;
import com.maco.spotify.api.client.SpotifyClient;
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
    private final Map<UUID, SpotifyClient> userClients = new ConcurrentHashMap<>();
    private final SpotifyClientFactory clientFactory;
    private final SpotifyDataService spotifyDataService;

    public SpotifyClient getOrCreateSpotifyClient(UUID userId) {
        log.info("Getting or creating Spotify client for userId: {}", userId);
        log.info("User clients map size: {}", userClients.size());
        log.info("User clients map: {}", userClients);
        log.info("Map contains userId {}: {}", userId, userClients.containsKey(userId));

        if (userClients.containsKey(userId)) {
            log.info("Returning existing Spotify client for userId: {}", userId);
            SpotifyClient client = userClients.get(userId);

            if(client.isAuthenticated()) {
                log.info("Client is authenticated");
                extractClientDetails(client);
            } else {
                log.info("Client is not authenticated");
            }
            return client;
        }

        log.info("Creating new Spotify client for userId: {}", userId);
        SpotifyClient client = clientFactory.createSpotifyClient();
        log.info("Created new Spotify client for userId: {}", userId);
        userClients.put(userId, client);
        log.info("Stored Spotify client for userId: {}", userId);
        return client;  // Return the SAME client you stored
    }

    private void extractClientDetails(SpotifyClient client) {
        if (client.isAuthenticated()) {
            log.info("Client details: {}", client.toString());
            log.info("Access Token: {}", client.getTokenManager().getCurrentToken().getAccessToken());
            log.info("Refresh Token: {}", client.getTokenManager().getCurrentToken().getRefreshToken());
            log.info("Expires In: {}", client.getTokenManager().getCurrentToken().getExpiresIn());
            log.info("Scope: {}", client.getTokenManager().getCurrentToken().getScope());
        }
    }

    public void updateClientToken(UUID userId, SpotifyClient client) {
        if (userClients.containsKey(userId)) {
            userClients.put(userId, client);
            log.info("Updated token for userId: {}", userId);
        }
    }

    public void cleanupExpiredClients() {
        userClients.entrySet().removeIf(entry -> {
            SpotifyClient client = entry.getValue();
            boolean expired = !client.isActive();
            if (expired) {
                log.info("Removing expired client for userId: {}", entry.getKey());
            }
            return expired;
        });
    }

    public void removeSpotifyClient(UUID userId) {
        userClients.remove(userId);
        log.info("Removed Spotify client for userId: {}", userId);
    }

    public boolean isUserAuthenticated(UUID userId) {
        if (!userClients.containsKey(userId)) {
            return false;
        }
        SpotifyClient client = userClients.get(userId);
        return client.isAuthenticated();
    }

    public void ensureUserAuthenticated(UUID userId) {
        if (!isUserAuthenticated(userId)) {
            throw new IllegalStateException("User " + userId + " is not authenticated");
        }
    }
}