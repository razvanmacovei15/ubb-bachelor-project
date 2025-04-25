package com.maco.followthebeat.controller;

import com.maco.followthebeat.entity.User;
import com.maco.followthebeat.service.interfaces.AuthService;
import com.maco.followthebeat.service.interfaces.StateCacheService;
import com.maco.followthebeat.service.interfaces.UserService;
import com.maco.followthebeat.spotify.client.SpotifyClientFactory;
import com.maco.followthebeat.spotify.client.SpotifyClientManager;
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
@RequestMapping("/spotify-auth")
public class SpotifyAuthController {

    private final SpotifyClientFactory clientFactory;
    private final SpotifyClientManager clientManager;
    private final StateCacheService stateCacheService;
    private final AuthService authService;
    private final UserService userService;

    @Autowired
    public SpotifyAuthController(
            SpotifyClientFactory clientFactory,
            StateCacheService stateCacheService,
            AuthService authService,
            UserService userService,
            SpotifyClientManager clientManager) {
        this.clientFactory = clientFactory;
        this.stateCacheService = stateCacheService;
        this.authService = authService;
        this.userService = userService;
        this.clientManager = clientManager;
    }

    @GetMapping("/auth-url")
    public ResponseEntity<String> getLoginUrl(@RequestParam String state) {
        try {
            log.info("Received state: {}", state);
            UUID userId = authService.ensureValidUser(state);

            stateCacheService.store(state, userId);
            log.info("Stored state {} for userId {}", state, userId);

            SpotifyClient client = clientManager.getOrCreateSpotifyClient(userId);
            String loginUrl = client.getAuthUrlWithState(state) + "&userId=" + userId;
            log.info("Generated login URL: {}", loginUrl);
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
        log.info("Retrieved userId {} for state {}", userId, state);
        if (userId == null) {
            return ResponseEntity.badRequest().body("Invalid or expired state");
        }

        try {
            log.info("Handling callback for userId {} with code {}", userId, code);
            // Create a client without tokens for initial auth
            SpotifyClient client = clientManager.getOrCreateSpotifyClient(userId);
            log.info("Client created for userId {}", userId);
            client.authenticateWithCode(code);
            log.info("Client authenticated for userId {}", userId);
            log.info("Client details: {}", client.getTokenManager().getCurrentToken().toString());

            User user = userService.findUserById(userId)
                    .orElseThrow(() -> new IllegalStateException("User not found for ID: " + userId));

            User linkedUser = authService.linkSpotifyAccount(user, client);

            return ResponseEntity.ok(
                    "<script>window.opener.postMessage({ userId: '" + linkedUser.getId() + "' }, '*'); window.close();</script>"
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Authentication failed: " + e.getMessage());
        }
    }



} 