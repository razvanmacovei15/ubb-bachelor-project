package com.maco.followthebeat.v2.spotify.tracks.controller;

import com.maco.followthebeat.v2.common.enums.SpotifyTimeRange;
import com.maco.followthebeat.v1.tracks.SpotifyTracksService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/spotify-tracks")
@RequiredArgsConstructor
public class SpotifyTrackController {
    private final SpotifyTracksService spotifyTracksService;

    @GetMapping("/top-tracks")
    public ResponseEntity<?> getTopTracks(
            @RequestParam(required = false) String userId,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0", required = false) int offset,
            @RequestParam(defaultValue = "medium_term") String range) {

        if (userId == null || userId.isEmpty()) {
            return ResponseEntity.badRequest().body("User ID is required");
        }

        try {
            UUID userUuid = UUID.fromString(userId);
            SpotifyTimeRange timeRange = Arrays.stream(SpotifyTimeRange.values())
                    .filter(r -> r.getValue().equals(range))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Invalid time range: " + range));
            
            return spotifyTracksService.fetchTopTracks(userUuid, timeRange, limit, offset);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
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