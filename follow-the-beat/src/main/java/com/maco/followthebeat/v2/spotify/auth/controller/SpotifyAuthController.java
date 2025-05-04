package com.maco.followthebeat.v2.spotify.auth.controller;

import com.maco.client.v2.SpotifyClientI;
import com.maco.followthebeat.v2.cache.RedisStateCacheService;
import com.maco.followthebeat.v2.cache.RedisStateCacheServiceImpl;
import com.maco.followthebeat.v2.spotify.auth.service.impl.StateCacheServiceImpl;
import com.maco.followthebeat.v2.user.entity.User;
import com.maco.followthebeat.v2.spotify.auth.service.interfaces.AuthService;
import com.maco.followthebeat.v2.spotify.auth.service.interfaces.StateCacheService;
import com.maco.followthebeat.v2.user.service.interfaces.UserService;
import com.maco.followthebeat.v2.spotify.auth.client.SpotifyClientManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/spotify-auth")
public class SpotifyAuthController {

    private final SpotifyClientManager clientManager;
    private final StateCacheService stateCacheService;
    private final RedisStateCacheServiceImpl redisStateCacheService;
    private final AuthService authService;
    private final UserService userService;

    @Autowired
    public SpotifyAuthController(
            StateCacheService stateCacheService,
            AuthService authService,
            UserService userService,
            SpotifyClientManager clientManager,
            RedisStateCacheServiceImpl redisStateCacheService) {
        this.stateCacheService = stateCacheService;
        this.authService = authService;
        this.userService = userService;
        this.clientManager = clientManager;
        this.redisStateCacheService = redisStateCacheService;
    }

    @GetMapping("/auth-url")
    public ResponseEntity<String> getLoginUrl(@RequestParam String state) {
        try {
            UUID userId = authService.ensureValidUser(state);

            redisStateCacheService.store(state, userId);

            SpotifyClientI client = clientManager.getOrCreateSpotifyClient(userId);

            String loginUrl = client.getAuthorizationUrl(state) + "&userId=" + userId;
            System.out.println(loginUrl);
            return ResponseEntity.ok(loginUrl);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/newCallback")
    public ResponseEntity<Void> handleNewCallback(
            @RequestParam String code,
            @RequestParam String state,
            @RequestParam(required = false) String error) {

        UUID userId = redisStateCacheService.retrieve(state);

        if (userId == null) {
            return ResponseEntity.status(302).location(URI.create("http://localhost:5173/spotify-auth-error")).build();
        }

        try {
            SpotifyClientI client = clientManager.getOrCreateSpotifyClient(userId);
            client.authenticate(code);

            UUID finalUserId = userId;

            if (!userService.hasConnectedSpotifyAccount(userId)) {
                User user = userService.findUserById(userId)
                        .orElseThrow(() -> new IllegalStateException("User not found for ID: " + userId));
                finalUserId = authService.linkSpotifyAccount(user, client).getId();
            }

            String sessionToken = UUID.randomUUID().toString();
            redisStateCacheService.storeSessionToken(state, sessionToken);
            redisStateCacheService.storeUserForSession(sessionToken, finalUserId);

            return ResponseEntity.status(302).location(URI.create("http://localhost:5173/spotify-auth-success?state=" + state)).build();
        } catch (Exception e) {
            return ResponseEntity.status(302).location(URI.create("http://localhost:5173/spotify-auth-error")).build();
        }

    }

    @GetMapping("/auth-status")
    public ResponseEntity<String> getAuthStatus(@RequestParam String state) {
        String sessionToken = redisStateCacheService.retrieveSessionToken(state);
        return sessionToken != null ? ResponseEntity.ok(sessionToken) : ResponseEntity.noContent().build();
    }
} 