package com.maco.followthebeat.v2.spotify.auth.client;

import com.maco.client.v2.SpotifyClientI;
import com.maco.followthebeat.v2.spotify.auth.mapper.TokenMapper;
import com.maco.followthebeat.v2.spotify.auth.userdata.entity.SpotifyUserData;
import com.maco.followthebeat.v2.spotify.auth.userdata.repo.SpotifyUserDataRepo;
import com.maco.followthebeat.v2.user.entity.User;
import com.maco.followthebeat.v2.user.service.interfaces.UserService;
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

    private final UserService userService;
    private final TokenMapper tokenMapper;
    private final SpotifyUserDataRepo spotifyUserDataRepo;

    public SpotifyClientI getOrCreateSpotifyClient(UUID userId) {
        if (userClients.containsKey(userId)) {
            return userClients.get(userId);
        }

        SpotifyClientI client = clientFactory.createSpotifyClient();

        client.setTokenUpdateListener(newToken -> {
            log.info("Persisting updated token for user {}", userId);
            User user = userService.findUserById(userId)
                    .orElseThrow(() -> new IllegalStateException("User not found"));
            SpotifyUserData entity = tokenMapper.toEntity(client, user);
            spotifyUserDataRepo.save(entity);
        });

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

    public void changeClientKey(UUID userId, UUID alreadyExistingUserId) {
        userClients.put(alreadyExistingUserId, userClients.get(userId));
        userClients.remove(userId);
    }
}