package com.maco.followthebeat.controller;

import com.maco.followthebeat.entity.User;
import com.maco.followthebeat.service.interfaces.AuthService;
import com.maco.followthebeat.service.interfaces.StateCacheService;
import com.maco.followthebeat.service.interfaces.UserService;
import com.maco.spotify.api.client.SpotifyClient;
import com.maco.spotify.api.model.SpotifyTrack;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/spotify-auth")
public class SpotifyAuthController {

    private final SpotifyClient spotifyClient;
    private final StateCacheService stateCacheService;
    private final AuthService authService;
    private final UserService userService;

    @Autowired
    public SpotifyAuthController(
            SpotifyClient spotifyClient,
            StateCacheService stateCacheService,
            AuthService authService,
            UserService userService) {
        this.spotifyClient = spotifyClient;
        this.stateCacheService = stateCacheService;
        this.authService = authService;
        this.userService = userService;
    }

    @GetMapping("/auth-url")
    public ResponseEntity<String> getLoginUrl(@RequestParam String state) {
        try {
            UUID userId = authService.ensureValidUser(state);
            stateCacheService.store(state, userId);

            String loginUrl = spotifyClient.getAuthUrlWithState(state) + "&userId=" + userId;
            return ResponseEntity.ok(loginUrl);
        } catch (Exception e) {
            log.error("Failed to generate login URL", e);
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/callback")
    public ResponseEntity<String> handleCallback(
            @RequestParam String code,
            @RequestParam String state,
            @RequestParam(required = false) String error) {

        if (error != null) {
            return ResponseEntity.badRequest().body("Spotify auth failed: " + error);
        }

        UUID userId = stateCacheService.retrieve(state);
        if (userId == null) {
            return ResponseEntity.badRequest().body("Invalid or expired state");
        }

        try {
            spotifyClient.authenticateWithCode(code);

            User user = userService.findUserById(userId)
                    .orElseThrow(() -> new IllegalStateException("User not found for ID: " + userId));
            User linbkedUser = authService.linkSpotifyAccount(user, spotifyClient);

            return ResponseEntity.ok(
                    "<script>window.opener.postMessage({ userId: '" + linbkedUser.getId() + "' }, '*'); window.close();</script>"
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Authentication failed: " + e.getMessage());
        }
    }



} 