package com.maco.followthebeat.controller;

import com.maco.followthebeat.entity.SpotifyUserData;
import com.maco.followthebeat.entity.User;
import com.maco.followthebeat.enums.SpotifyTimeRange;
import com.maco.spotify.api.enums.TimeRange;
import com.maco.followthebeat.service.interfaces.UserService;
import com.maco.followthebeat.spotify.client.SpotifyClientManager;
import com.maco.followthebeat.spotify.service.SpotifyDataService;
import com.maco.spotify.api.client.SpotifyClient;
import com.maco.spotify.api.model.SpotifyArtist;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/spotify-artists")
public class SpotifyArtistsController {
    private final SpotifyClientManager clientManager;
    private final SpotifyDataService platformService;
    private final UserService userService;

    @Autowired
    public SpotifyArtistsController(
            SpotifyClientManager clientManager,
            SpotifyDataService platformService,
            UserService userService) {
        this.clientManager = clientManager;
        this.platformService = platformService;
        this.userService = userService;
    }

    @GetMapping("/top-artists")
    public ResponseEntity<?> getTopArtists(
            @RequestParam(required = false) String userId,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0", required = false) int offset,
            @RequestParam(defaultValue = "ALL_TIME") SpotifyTimeRange range) {

        if (userId == null || userId.isEmpty()) {
            log.warn("No userId provided in request");
            return ResponseEntity.badRequest().body("User ID is required");
        }

        try {
            UUID userUuid = UUID.fromString(userId);
            return fetchTopArtists(userUuid, limit, offset, range);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid UUID format: {}", userId);
            return ResponseEntity.badRequest().body("Invalid user ID format");
        }
    }

    private ResponseEntity<?> fetchTopArtists(UUID userId, int limit, int offset, SpotifyTimeRange spotifyRange) {
        try {
            // 1. Check if user exists
            Optional<User> userOptional = userService.findUserById(userId);
            if (userOptional.isEmpty()) {
                log.warn("User {} not found", userId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("User not found");
            }

            // 2. Get authenticated client from manager
            SpotifyClient client = clientManager.getOrCreateSpotifyClient(userId);
            if (!client.isAuthenticated()) {
                log.warn("User {} is not authenticated with Spotify", userId);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("User is not authenticated with Spotify. Please authenticate first.");
            }

            // 3. Convert SpotifyTimeRange to TimeRange
            TimeRange range = convertTimeRange(spotifyRange);

            // 4. Fetch artists based on time range
            List<SpotifyArtist> topArtists = switch (range) {
                case SHORT_TERM -> client.getTopArtistsLast4Weeks(limit, offset);
                case MEDIUM_TERM -> client.getTopArtistsLast6Months(limit, offset);
                case LONG_TERM -> client.getTopArtistsAllTime(limit, offset);
                default -> throw new IllegalArgumentException("Unsupported time range: " + range);
            };

            log.info("Successfully retrieved {} top artists for user {} in range {}",
                    topArtists.size(), userId, range);
            return ResponseEntity.ok(topArtists);

        } catch (IllegalStateException e) {
            log.warn("Spotify client error for user {}: {}", userId, e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Authentication error: " + e.getMessage());

        } catch (IllegalArgumentException e) {
            log.warn("Invalid request for user {}: {}", userId, e.getMessage());
            return ResponseEntity.badRequest()
                    .body("Invalid request: " + e.getMessage());

        } catch (Exception e) {
            log.error("Unexpected error fetching top artists for user {}: {}", userId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred");
        }
    }

    private TimeRange convertTimeRange(SpotifyTimeRange spotifyRange) {
        return switch (spotifyRange) {
            case LAST_4_WEEKS -> TimeRange.SHORT_TERM;
            case LAST_6_MONTHS -> TimeRange.MEDIUM_TERM;
            case ALL_TIME -> TimeRange.LONG_TERM;
            default -> throw new IllegalArgumentException("Unsupported time range: " + spotifyRange);
        };
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalStateException(IllegalStateException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }
}