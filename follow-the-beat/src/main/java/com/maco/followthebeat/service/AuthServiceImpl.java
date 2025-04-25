package com.maco.followthebeat.service;

import com.maco.followthebeat.entity.SpotifyUserData;
import com.maco.followthebeat.entity.User;
import com.maco.followthebeat.service.interfaces.AuthService;
import com.maco.followthebeat.spotify.service.SpotifyDataService;
import com.maco.followthebeat.service.interfaces.UserService;
import com.maco.spotify.api.client.SpotifyClient;
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
    private final SpotifyDataService spotifyDataService;

    @Override
    public UUID ensureValidUser(String state) {
        UUID userId;
        // Check if the state is a valid UUID
        log.info("Ensuring valid user for state: {}", state);
        try {
            userId = UUID.fromString(state);
            log.info("Parsed user ID from state: {}", userId);
            Optional<User> maybeExisting = userService.findUserById(userId);
            // Check if the user ID exists in the database
            log.info("Checking if user ID exists in DB: {}", userId);
            log.info("User ID exists in DB: {}", maybeExisting.isPresent());
            if (maybeExisting.isPresent()) {
                return userId;
            } else {
                log.warn("User ID parsed but not found in DB, creating new anonymous user.");
                UUID anonymousUserId = userService.createAnonymousUser();
                log.info("Created anonymous user with ID: {}", anonymousUserId);
                return anonymousUserId;
            }

        } catch (IllegalArgumentException e) {
            // State wasn't even a valid UUID
            log.warn("Invalid UUID format in state, creating new anonymous user.");
            UUID anonymousUserId = userService.createAnonymousUser();
            log.info("Created anonymous user with ID: {}", anonymousUserId);
            return anonymousUserId;
        }
    }

    @Override
    @Transactional
    public User linkSpotifyAccount(User user, SpotifyClient client) {
        String spotifyUserId = client.getUserDetails().getId();
        log.info("Linking Spotify account for user: {}", user.getId());
        Optional<User> maybeExisting = userService.findUserBySpotifyId(spotifyUserId);

        if (maybeExisting.isPresent()) {
            if (user.isAnonymous()) {
                userService.mergeAnonymousUser(user, maybeExisting.get());
            }
            return maybeExisting.get();
        } else {
            SpotifyUserData sp = spotifyDataService.createSpotifyData(client, user);
            user.setSpotifyUserData(sp);
            userService.updateUser(user);
            return user;
        }
    }

}
