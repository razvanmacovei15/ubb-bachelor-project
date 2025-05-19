package com.maco.followthebeat.v2.spotify.auth.controller;

import com.maco.client.v2.SpotifyClient;
import com.maco.followthebeat.v2.cache.RedisStateCacheServiceImpl;
import com.maco.followthebeat.v2.common.exceptions.UserNotFoundException;
import com.maco.followthebeat.v2.user.entity.User;
import com.maco.followthebeat.v2.spotify.auth.service.interfaces.AuthService;
import com.maco.followthebeat.v2.user.service.interfaces.UserListeningProfileService;
import com.maco.followthebeat.v2.user.service.interfaces.UserService;
import com.maco.followthebeat.v2.spotify.auth.client.SpotifyClientManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

        log.info("[/newCallback] Received callback with state={}, code={}, error={}", state, code, error);

        UUID userId = redisStateCacheService.retrieve(state);

        if (userId == null) {
            log.warn("[/newCallback] No user found in Redis for state: {}", state);
            return ResponseEntity.status(302).location(URI.create(frontendUrl + "/spotify-auth-error")).build();
        }

        try {
            log.info("[/newCallback] Retrieved userId={} from Redis for state={}", userId, state);

            SpotifyClient client = clientManager.getOrCreateSpotifyClient(userId);
            log.info("[/newCallback] SpotifyClient created, attempting authentication...");

            client.authenticate(code);

            String spotifyId = client.getCurrentUserDetails().getId();
            log.info("[/newCallback] Authenticated with Spotify, retrieved spotifyId={}", spotifyId);

            Optional<User> alreadyUser = userService.findUserBySpotifyId(spotifyId);
            UUID finalUserId;

            if (alreadyUser.isPresent()) {
                log.info("[/newCallback] Spotify account already linked to userId={}", alreadyUser.get().getId());
                clientManager.changeClientKey(userId, alreadyUser.get().getId());
                finalUserId = alreadyUser.get().getId();
            } else {
                log.info("[/newCallback] Linking new Spotify account to userId={}", userId);
                User user = userService.findUserById(userId)
                        .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " does not exist."));
                finalUserId = authService.linkSpotifyAccountAndStoreListeningProfile(user, client).getId();
                log.info("[/newCallback] Linked Spotify account to userId={}, updated profile", finalUserId);
            }

            String sessionToken = UUID.randomUUID().toString();
            redisStateCacheService.storeUserForSession(sessionToken, finalUserId);
            redisStateCacheService.storeSessionToken(state, sessionToken);

            log.info("[/newCallback] Stored sessionToken={} for userId={}, redirecting to success", sessionToken, finalUserId);
            return ResponseEntity.status(302).location(URI.create(frontendUrl + "/spotify-auth-success?state=" + state)).build();

        } catch (Exception e) {
            log.error("[/newCallback] Exception occurred for state={}", state, e);
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

    @GetMapping("/is-connected")
    public ResponseEntity<Boolean> isUserConnectedToSpotify(@RequestParam String sessionToken) {
        try {
            UUID userId = redisStateCacheService.getUserBySession(sessionToken);
            if (userId == null) {
                return ResponseEntity.ok(false);
            }

            Optional<User> user = userService.findUserById(userId);
            if (user.isEmpty()) {
                return ResponseEntity.ok(false);
            }

            SpotifyClient client = clientManager.getOrCreateSpotifyClient(userId);
            return ResponseEntity.ok(client.isAuthenticated());
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }
} 