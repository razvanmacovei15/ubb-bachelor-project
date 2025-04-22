package com.maco.followthebeat.controller;

import com.maco.followthebeat.entity.SpotifyPlatform;
import com.maco.followthebeat.entity.User;
import com.maco.followthebeat.mapper.TokenMapper;
import com.maco.followthebeat.service.SpotifyPlatformService;
import com.maco.followthebeat.service.StateCacheService;
import com.maco.followthebeat.service.UserService;
import com.maco.spotify.api.client.SpotifyClient;
import com.maco.spotify.api.model.SpotifyTrack;
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
    public ResponseEntity<String> getLoginUrl(@RequestParam String state, @RequestParam(required = false) UUID userId) {
        log.info("Generating Spotify login URL");
        try {
            // Generate a unique state parameter for this user
            if (userId == null) {
                log.info("the user id is: {}", userId);
                log.info("the state is: {}", state);
                userId = userService.createAnonymousUser();
//                stateCacheService.store(state, userId);
                log.info("Created new anonymous user: {}", userId);
            } else {
                log.info("Using existing user ID: {}", userId);
                User user = userService.getUser(userId);
                //load spotify related login credentials from DB
            }

            stateCacheService.store(state, userId);

            String loginUrl = spotifyClient.getAuthUrlWithState(state);
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
        
        log.info("Received Spotify callback with code: {}, state: {}, error: {}", code, state, error);

        if (error != null) {
            log.error("Spotify authorization error: {}", error);
            return ResponseEntity.badRequest().body("Authorization failed: " + error);
        }

        UUID userId = stateCacheService.retrieve(state);

        if(userId == null){
            return ResponseEntity.badRequest().body("Invalid or expired state");
        }

        try {
            User user = userService.getUser(userId);

            spotifyClient.authenticateWithCode(code);

            SpotifyPlatform spotifyPlatform = tokenMapper.toEntity(this.spotifyClient, user);

            spotifyPlatformService.createSpotifyPlatform(spotifyPlatform);

            stateCacheService.invalidate(state);

            log.info("Successfully authenticated with Spotify");
            return ResponseEntity.ok("<script>window.close();</script>");
        } catch (Exception e) {
            log.error("Authentication failed: {}", e.getMessage(), e);
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