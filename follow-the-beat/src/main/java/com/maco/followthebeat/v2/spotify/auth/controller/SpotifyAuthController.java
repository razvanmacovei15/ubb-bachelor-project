package com.maco.followthebeat.v2.spotify.auth.controller;

import com.maco.client.v2.SpotifyClient;
import com.maco.followthebeat.v2.cache.RedisStateCacheServiceImpl;
import com.maco.followthebeat.v2.common.exceptions.UserNotFoundException;
import com.maco.followthebeat.v2.user.entity.User;
import com.maco.followthebeat.v2.spotify.auth.service.interfaces.AuthService;
import com.maco.followthebeat.v2.user.service.interfaces.UserListeningProfileService;
import com.maco.followthebeat.v2.user.service.interfaces.UserService;
import com.maco.followthebeat.v2.spotify.auth.client.SpotifyClientManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/spotify-auth")
public class SpotifyAuthController {

    private final SpotifyClientManager clientManager;
    private final RedisStateCacheServiceImpl redisStateCacheService;
    private final AuthService authService;
    private final UserService userService;

    @Value("${frontend.url}")
    private String frontendUrl;

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
            SpotifyClient client = clientManager.getOrCreateSpotifyClient(userId);
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
            return ResponseEntity.status(302).location(URI.create(frontendUrl + "/spotify-auth-error")).build();
        }

        try {
            SpotifyClient client = clientManager.getOrCreateSpotifyClient(userId);
            client.authenticate(code);
            
            UUID finalUserId = userId;
            String spotifyId = client.getCurrentUserDetails().getId();
            Optional<User> alreadyUser = userService.findUserBySpotifyId(spotifyId);

            if(alreadyUser.isPresent()) {
                clientManager.changeClientKey(userId, alreadyUser.get().getId());
                finalUserId = alreadyUser.get().getId();
            } else {
                User user = userService.findUserById(userId)
                        .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " does not exist."));
                finalUserId = authService.linkSpotifyAccountAndStoreListeningProfile(user, client).getId();
            }

            String sessionToken = UUID.randomUUID().toString();
            redisStateCacheService.storeUserForSession(sessionToken, finalUserId);
            redisStateCacheService.storeSessionToken(state, sessionToken);

            return ResponseEntity.status(302).location(URI.create(frontendUrl + "/spotify-auth-success?state=" + state)).build();
        } catch (Exception e) {
            return ResponseEntity.status(302).location(URI.create(frontendUrl + "/spotify-auth-error")).build();
        }
    }

    @GetMapping("/auth-status")
    public ResponseEntity<String> getAuthStatus(@RequestParam String state) {
        String sessionToken = redisStateCacheService.retrieveSessionToken(state);
        
        if (sessionToken == null) {
            UUID userId = redisStateCacheService.retrieve(state);
            
            if (userId != null) {
                sessionToken = redisStateCacheService.getSessionTokenForUser(userId);
                
                if (sessionToken != null) {
                    redisStateCacheService.storeSessionToken(state, sessionToken);
                }
            }
        }
        
        if (sessionToken != null) {
            return ResponseEntity.ok(sessionToken);
        } else {
            return ResponseEntity.noContent().build();
        }
    }
} 