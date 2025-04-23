package com.maco.followthebeat.controller;

import com.maco.followthebeat.entity.SpotifyPlatform;
import com.maco.followthebeat.entity.User;
import com.maco.followthebeat.mapper.TokenMapper;
import com.maco.followthebeat.service.SpotifyPlatformService;
import com.maco.followthebeat.service.StateCacheService;
import com.maco.followthebeat.service.UserService;
import com.maco.spotify.api.client.SpotifyClient;
import com.maco.spotify.api.model.SpotifyTrack;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/spotify")
public class SpotifyController {

    private final SpotifyClient spotifyClient;
    private final UserService userService;
    private final SpotifyPlatformService spotifyPlatformService;
    private final StateCacheService cacheService;
    private final StateCacheService stateCacheService;
    private final TokenMapper tokenMapper;

    @Autowired
    public SpotifyController(SpotifyClient spotifyClient, UserService userService,SpotifyPlatformService spotifyPlatformService, StateCacheService cacheService, StateCacheService stateCacheService, TokenMapper tokenMapper) {
        this.spotifyClient = spotifyClient;
        this.userService = userService;
        this.spotifyPlatformService = spotifyPlatformService;
        this.cacheService = cacheService;
        this.stateCacheService = stateCacheService;
        this.tokenMapper = tokenMapper;
    }

    @GetMapping("/auth-url")
    public ResponseEntity<String> getLoginUrl(@RequestParam String state) {
        log.info("Generating Spotify login URL");
        try {
            // Try to parse the state as UUID to check if it's an existing user ID
            UUID userId = null;
            try {
                userId = UUID.fromString(state);
                // If we got here, it's a valid UUID - check if user exists
                try {
                    User user = userService.getUser(userId);
                    // Check if user already has Spotify connected
                    SpotifyPlatform sp = spotifyPlatformService.getSpotifyPlatformByUser(user);
                    if (sp != null) {
                        return ResponseEntity.badRequest().body("User already has Spotify connected");
                    }
                } catch (EntityNotFoundException e) {
                    // If user doesn't exist, create new anonymous user
                    userId = userService.createAnonymousUser();
                }
            } catch (IllegalArgumentException e) {
                // If state is not a valid UUID, treat as new user
                userId = userService.createAnonymousUser();
            }

            stateCacheService.store(state, userId);

            String loginUrl = spotifyClient.getAuthUrlWithState(state) + "&userId=" + userId.toString();
            log.info("url: {}", loginUrl);
            return ResponseEntity.ok(loginUrl);
        } catch (Exception e) {
            log.error("Failed to generate login URL: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body("Failed to generate login URL: " + e.getMessage());
        }
    }

    @GetMapping("/callback")
    public ResponseEntity<String> handleCallback(
            @RequestParam String code,
            @RequestParam String state,
            @RequestParam(required = false) String error) {

        try {
            // Get the userId from state cache
            UUID userId = stateCacheService.retrieve(state);
            if (userId == null) {
                return ResponseEntity.badRequest().body("Invalid or expired state");
            }

            // Authenticate with Spotify
            spotifyClient.authenticateWithCode(code);

            // Get Spotify user ID
            String spotifyUserId = spotifyClient.getUserDetails().getId();

            // Check if this Spotify account is already connected to another user
            User existingUser = userService.getUserBySpotifyId(spotifyUserId);

            if (existingUser != null) {
                // If we have an anonymous user, merge it with the existing user
                User currentUser = userService.getUser(userId);
                if (currentUser.isAnonymous()) {
                    userService.mergeAnonymousUser(currentUser, existingUser);
                }
                userId = existingUser.getId();
            } else {
                // Update the user with Spotify info
                User user = userService.getUser(userId);
                SpotifyPlatform newUserSpotifyPlatform = spotifyPlatformService.createSpotifyPlatform(this.spotifyClient, user);
                user.setSpotifyPlatform(newUserSpotifyPlatform);
                userService.updateUser(user.getId());
            }

            stateCacheService.invalidate(state);
            return ResponseEntity.ok("<script>window.close();</script>");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Authentication failed: " + e.getMessage());
        }
    }


    @GetMapping("/top-tracks")
    public ResponseEntity<List<SpotifyTrack>> getTopTracks(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String state) {
        
        log.info("Received request for top tracks with limit: {}, state: {}", limit, state);
        
        // TODO: Validate state and get user's Spotify client
        
        try {
            List<SpotifyTrack> topTracks = spotifyClient.getTopTracksAllTime(limit, 0);
            log.info("Successfully retrieved {} top tracks", topTracks.size());
            return ResponseEntity.ok(topTracks);
        } catch (Exception e) {
            log.error("Failed to get top tracks: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }
} 