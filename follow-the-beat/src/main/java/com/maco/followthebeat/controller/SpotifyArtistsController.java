package com.maco.followthebeat.controller;

import com.maco.client.v2.model.SpotifyArtist;
import com.maco.followthebeat.dto.spotify.SpotifyArtistDto;
import com.maco.followthebeat.entity.User;
import com.maco.followthebeat.entity.spotify.interfaces.BaseUserTopArtist;
import com.maco.followthebeat.enums.SpotifyTimeRange;
import com.maco.followthebeat.mapper.SpotifyArtistMapper;
import com.maco.followthebeat.service.interfaces.UserService;
import com.maco.followthebeat.service.spotify.interfaces.SpotifyArtistStatsService;
import com.maco.followthebeat.spotify.api.service.artists.SpotifyApiArtistsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/spotify-artists")
@RequiredArgsConstructor
public class SpotifyArtistsController {
    private final SpotifyApiArtistsService spotifyApiArtistsService;
    private final SpotifyArtistStatsService spotifyArtistStatsService;
    private final UserService userService;
    private final SpotifyArtistMapper spotifyArtistMapper;

    @GetMapping(value = "/top-artists", produces = "application/json")
    public ResponseEntity<?> getTopArtists(
            @RequestParam(required = false) String userId,
            @RequestParam(defaultValue = "50") int limit,
            @RequestParam(defaultValue = "0", required = false) int offset,
            @RequestParam(defaultValue = "medium_term") String range) {

        log.info("Received request for top artists with userId: {}, range: {}", userId, range);

        if (userId == null || userId.isEmpty()) {
            return ResponseEntity.badRequest().body("User ID is required");
        }

        try {
            UUID userUuid = UUID.fromString(userId);
            User user = userService.findUserById(userUuid)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            SpotifyTimeRange timeRange = SpotifyTimeRange.fromString(range);

            List<? extends BaseUserTopArtist> stats = spotifyArtistStatsService.getArtistStatsByTimeRange(user, timeRange);

            if (!stats.isEmpty()) {
                List<SpotifyArtistDto> dtos = stats.stream()
                        .map(stat -> {
                            SpotifyArtistDto dto = spotifyArtistMapper.entityToDto(stat.getArtist());
                            dto.setRank(stat.getRank());
                            log.info("Fetched stats from database for userId: {}, range: {}, artist: {}", userId, range, dto.getName());
                            return dto;
                        })
                        .collect(Collectors.toList());
                return ResponseEntity.ok(dtos);
            }

            log.info("No stats found in database for userId: {}, range: {}", userId, range);
            // If no stats in database, fetch from Spotify API
            spotifyArtistStatsService.fetchAndSaveInitialStats(user);
            ResponseEntity<?> response = spotifyApiArtistsService.fetchTopArtists(userUuid, timeRange, limit, offset);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() instanceof List<?> artists) {
                List<SpotifyArtistDto> dtos = artists.stream()
                        .map(artist -> spotifyArtistMapper.clientToDto((SpotifyArtist) artist))
                        .collect(Collectors.toList());
                return ResponseEntity.ok(dtos);
            }
            return response;
        } catch (IllegalArgumentException e) {
            log.error("Error processing request: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "/refresh", produces = "application/json")
    public ResponseEntity<?> refreshStats(
            @RequestParam String userId,
            @RequestParam(defaultValue = "medium_term") String range) {
        log.info("Received refresh request for userId: {}, range: {}", userId, range);

        try {
            UUID userUuid = UUID.fromString(userId);
            User user = userService.findUserById(userUuid)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            SpotifyTimeRange timeRange = SpotifyTimeRange.valueOf(range);

            spotifyArtistStatsService.updateStatsByTimeRange(user, timeRange);

            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            log.error("Error processing refresh request: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("Illegal argument exception: {}", e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalStateException(IllegalStateException e) {
        log.error("Illegal state exception: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }
}