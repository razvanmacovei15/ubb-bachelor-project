package com.maco.followthebeat.v2.spotify.auth.controller;

import com.maco.client.v2.SpotifyClientI;
import com.maco.followthebeat.v2.cache.RedisStateCacheService;
import com.maco.followthebeat.v2.cache.RedisStateCacheServiceImpl;
import com.maco.followthebeat.v2.common.exceptions.UserNotFoundException;
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
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/spotify-auth")
public class SpotifyAuthController {

    private final SpotifyClientManager clientManager;
    private final RedisStateCacheServiceImpl redisStateCacheService;
    private final AuthService authService;
    private final UserService userService;

    @Autowired
    public SpotifyAuthController(
            AuthService authService,
            UserService userService,
            SpotifyClientManager clientManager,
            RedisStateCacheServiceImpl redisStateCacheService) {
        this.authService = authService;
        this.userService = userService;
        this.clientManager = clientManager;
        this.redisStateCacheService = redisStateCacheService;
    }

    @GetMapping("/auth-url")
    public ResponseEntity<String> getLoginUrl() {
        try {
            UUID userId = userService.createAnonymousUser();
            UUID stateUUID = UUID.randomUUID();
            redisStateCacheService.store(stateUUID.toString(), userId);

            SpotifyClientI client = clientManager.getOrCreateSpotifyClient(userId);

            String loginUrl = client.getAuthorizationUrl(stateUUID.toString());
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
            return ResponseEntity.status(302).location(URI.create("http://localhost:8010/spotify-auth-error")).build();
        }

        try {
            SpotifyClientI client = clientManager.getOrCreateSpotifyClient(userId);
            client.authenticate(code);
            //at this point client should be authenticated
            UUID finalUserId = userId;

            String spotifyId = client.getCurrentUserDetails().getId();
            //check if user already exists with this spotifyId
            Optional<User> alreadyUser = userService.findUserBySpotifyId(spotifyId);
            if(alreadyUser.isPresent()){
                //user has already connected their spotify account
                clientManager.changeClientKey(userId, alreadyUser.get().getId());
                finalUserId = alreadyUser.get().getId();
            } else {
                User user = userService.findUserById(userId)
                        .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " does not exist."));

                finalUserId = authService.linkSpotifyAccount(user, client).getId();
            }
            String sessionToken = redisStateCacheService.getSessionTokenForUser(finalUserId);

            if (sessionToken == null) {
                sessionToken = UUID.randomUUID().toString();
                redisStateCacheService.storeUserForSession(sessionToken, finalUserId);
                redisStateCacheService.storeSessionToken(state, sessionToken);
            }
            return ResponseEntity.status(302).location(URI.create("http://localhost:8010/spotify-auth-success?state=" + state)).build();
        } catch (Exception e) {
            return ResponseEntity.status(302).location(URI.create("http://localhost:8010/spotify-auth-error")).build();
        }

    }

    @GetMapping("/auth-status")
    public ResponseEntity<String> getAuthStatus(@RequestParam String state) {
        String sessionToken = redisStateCacheService.retrieveSessionToken(state);
        return sessionToken != null ? ResponseEntity.ok(sessionToken) : ResponseEntity.noContent().build();
    }
} 