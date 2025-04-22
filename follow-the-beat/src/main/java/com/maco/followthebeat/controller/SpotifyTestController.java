package com.maco.followthebeat.controller;

import com.maco.spotify.api.client.SpotifyClient;
import com.maco.spotify.api.model.SpotifyTrack;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/spotify-test")
public class SpotifyTestController {

    private final SpotifyClient spotifyClient;

    @Autowired
    public SpotifyTestController(SpotifyClient spotifyClient) {
        this.spotifyClient = spotifyClient;
    }

    @GetMapping("/login-url")
    public ResponseEntity<String> getLoginUrl() {
        log.info("Generating Spotify login URL");
        try {
            // Generate a unique state parameter for this user
            String state = UUID.randomUUID().toString();
            // TODO: Store this state in session/database with user info
            
            String loginUrl = spotifyClient.getAuthUrlWithState(state);
            return ResponseEntity.ok(loginUrl);
        } catch (Exception e) {
            log.error("Failed to generate login URL: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body("Failed to generate login URL: " + e.getMessage());
        }
    }

    @GetMapping("/callback")
    public ResponseEntity<String> handleCallback(
            @RequestParam String code,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String error) {
        
        log.info("Received Spotify callback with code: {}, state: {}, error: {}", code, state, error);
        
        // TODO: Validate the state parameter against stored state
        
        if (error != null) {
            log.error("Spotify authorization error: {}", error);
            return ResponseEntity.badRequest().body("Authorization failed: " + error);
        }

        try {
            spotifyClient.authenticateWithCode(code);
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