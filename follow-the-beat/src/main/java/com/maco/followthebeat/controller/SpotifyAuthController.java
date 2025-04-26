package com.maco.followthebeat.controller;

import com.maco.client.v2.SpotifyClientI;
import com.maco.followthebeat.entity.User;
import com.maco.followthebeat.service.interfaces.AuthService;
import com.maco.followthebeat.service.interfaces.StateCacheService;
import com.maco.followthebeat.service.interfaces.UserService;
import com.maco.followthebeat.spotify.client.SpotifyClientFactory;
import com.maco.followthebeat.spotify.client.SpotifyClientManager;
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

    private final SpotifyClientManager clientManager;
    private final StateCacheService stateCacheService;
    private final AuthService authService;
    private final UserService userService;

    @Autowired
    public SpotifyAuthController(
            StateCacheService stateCacheService,
            AuthService authService,
            UserService userService,
            SpotifyClientManager clientManager) {
        this.stateCacheService = stateCacheService;
        this.authService = authService;
        this.userService = userService;
        this.clientManager = clientManager;
    }

    @GetMapping("/auth-url")
    public ResponseEntity<String> getLoginUrl(@RequestParam String state) {
        try {
            UUID userId = authService.ensureValidUser(state);

            stateCacheService.store(state, userId);

            SpotifyClientI client = clientManager.getOrCreateSpotifyClient(userId);

            String loginUrl = client.getAuthorizationUrl(state) + "&userId=" + userId;

            return ResponseEntity.ok(loginUrl);
        } catch (Exception e) {
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
            SpotifyClientI client = clientManager.getOrCreateSpotifyClient(userId);

            client.authenticate(code);

            if(!userService.hasConnectedSpotifyAccount(userId)) {
                User user = userService.findUserById(userId)
                        .orElseThrow(() -> new IllegalStateException("User not found for ID: " + userId));

                User linkedUser = authService.linkSpotifyAccount(user, client);

                return ResponseEntity.ok(
                        "<script>window.opener.postMessage({ userId: '" + linkedUser.getId() + "' }, '*'); window.close();</script>"
                );
            }
            return ResponseEntity.ok(
                    "<script>window.opener.postMessage({ userId: '" + userId + "' }, '*'); window.close();</script>"
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Authentication failed: " + e.getMessage());
        }
    }



} 