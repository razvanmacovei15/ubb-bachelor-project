package com.maco.followthebeat.spotify.api.service.tracks;

import com.maco.client.v2.SpotifyClientI;
import com.maco.client.v2.model.SpotifyTrack;
import com.maco.followthebeat.enums.SpotifyTimeRange;
import com.maco.followthebeat.service.interfaces.UserService;
import com.maco.followthebeat.spotify.api.client.SpotifyClientManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpotifyTracksService {
    private final SpotifyClientManager clientManager;
    private final UserService userService;

    public ResponseEntity<?> fetchTopTracks(UUID userId, SpotifyTimeRange range, int limit, int offset) {
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

            // Fetch tracks based on time range
            List<SpotifyTrack> topTracks = switch (range) {
                case SHORT_TERM -> client.getTopTracksLast4Weeks(limit, offset);
                case MEDIUM_TERM -> client.getTopTracksLast6Months(limit, offset);
                case LONG_TERM -> client.getTopTracksAllTime(limit, offset);
                default -> throw new IllegalArgumentException("Unsupported time range: " + range);
            };

            return ResponseEntity.ok(topTracks);

        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred");
        }
    }
}
