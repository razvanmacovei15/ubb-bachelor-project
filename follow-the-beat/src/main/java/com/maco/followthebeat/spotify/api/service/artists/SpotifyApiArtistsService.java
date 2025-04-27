package com.maco.followthebeat.spotify.api.service.artists;

import com.maco.client.v2.SpotifyClientI;
import com.maco.client.v2.model.SpotifyArtist;
import com.maco.followthebeat.enums.SpotifyTimeRange;
import com.maco.followthebeat.service.interfaces.UserService;
import com.maco.followthebeat.spotify.api.client.SpotifyClientManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpotifyApiArtistsService {
    private final SpotifyClientManager clientManager;
    private final UserService userService;

    public ResponseEntity<?> fetchTopArtists(UUID userId, SpotifyTimeRange range, int limit, int offset) {
        try {
            // Validate user and Spotify authentication
            if (!userService.validateUserAndSpotifyAuth(userId, clientManager)) {
                // Get the client to check the specific error
                SpotifyClientI client = clientManager.getOrCreateSpotifyClient(userId);
                if (!client.isAuthenticated()) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body("User is not authenticated with Spotify. Please authenticate first.");
                }
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("User not found");
            }

            // Get the authenticated client
            SpotifyClientI client = clientManager.getOrCreateSpotifyClient(userId);

            // Fetch artists based on time range
            List<SpotifyArtist> topArtists = switch (range) {
                case SHORT_TERM -> client.getTopArtistsLast4Weeks(limit, offset);
                case MEDIUM_TERM -> client.getTopArtistsLast6Months(limit, offset);
                case LONG_TERM -> client.getTopArtistsAllTime(limit, offset);
                default -> throw new IllegalArgumentException("Unsupported time range: " + range);
            };

            return ResponseEntity.ok(topArtists);

        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Authentication error: " + e.getMessage());

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body("Invalid request: " + e.getMessage());

        } catch (DataIntegrityViolationException e) {
            log.warn("Duplicate artist detected, skipping insert: {}", e.getMessage());
            // Ignore and continue saving other artists
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Duplicate artist detected, skipping insert: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred");
        }
    }
}
