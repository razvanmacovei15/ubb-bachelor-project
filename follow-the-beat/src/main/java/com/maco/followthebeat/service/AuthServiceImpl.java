package com.maco.followthebeat.service;

import com.maco.followthebeat.entity.SpotifyPlatform;
import com.maco.followthebeat.entity.User;
import com.maco.followthebeat.service.interfaces.AuthService;
import com.maco.followthebeat.service.interfaces.SpotifyPlatformService;
import com.maco.followthebeat.service.interfaces.UserService;
import com.maco.spotify.api.client.SpotifyClient;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final SpotifyPlatformService spotifyPlatformService;

    @Override
    public UUID ensureValidUser(String state) {
        UUID userId;

        try {
            userId = UUID.fromString(state);
            Optional<User> maybeExisting = userService.findUserById(userId);

            if (maybeExisting.isPresent()) {
                boolean alreadyLinked = spotifyPlatformService
                        .getSpotifyPlatformByUser(maybeExisting.get())
                        .isPresent();

                if (alreadyLinked) {
                    throw new IllegalStateException("User already has Spotify connected");
                }

                return userId;
            } else {
                log.warn("User ID parsed but not found in DB, creating new anonymous user.");
                return userService.createAnonymousUser();
            }

        } catch (IllegalArgumentException e) {
            // State wasn't even a valid UUID
            log.warn("Invalid UUID format in state, creating new anonymous user.");
            return userService.createAnonymousUser();
        }
    }

    @Override
    @Transactional
    public User linkSpotifyAccount(User user, SpotifyClient client) {
        String spotifyUserId = client.getUserDetails().getId();
        Optional<User> maybeExisting = userService.findUserBySpotifyId(spotifyUserId);

        if (maybeExisting.isPresent()) {
            if (user.isAnonymous()) {
                userService.mergeAnonymousUser(user, maybeExisting.get());
            }
            return maybeExisting.get();
        } else {
            SpotifyPlatform sp = spotifyPlatformService.createSpotifyPlatform(client, user);
            user.setSpotifyPlatform(sp);
            userService.updateUser(user);
            return user;
        }
    }

}
