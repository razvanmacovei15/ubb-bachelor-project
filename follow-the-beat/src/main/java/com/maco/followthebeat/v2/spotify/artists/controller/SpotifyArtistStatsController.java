package com.maco.followthebeat.v2.spotify.artists.controller;

import com.maco.followthebeat.v2.user.entity.User;
import com.maco.followthebeat.v2.spotify.enums.SpotifyTimeRange;
import com.maco.followthebeat.v2.user.service.interfaces.UserService;
import com.maco.followthebeat.v2.spotify.artists.service.interfaces.SpotifyArtistStatsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/spotify/artists/stats")
@RequiredArgsConstructor
public class SpotifyArtistStatsController {
    private final SpotifyArtistStatsService spotifyArtistStatsService;
    private final UserService userService;

    @GetMapping("/short-term")
    public ResponseEntity<?> getShortTermArtists(@RequestParam UUID userId) {
        try {
            User user = userService.findUserById(userId).orElseThrow(
                    () -> new IllegalArgumentException("User not found"));
            return ResponseEntity.ok(spotifyArtistStatsService.getArtistStatsByTimeRange(user, SpotifyTimeRange.SHORT_TERM));
        } catch (Exception e) {
            log.error("Error fetching short-term artists", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/medium-term")
    public ResponseEntity<?> getMediumTermArtists(@RequestParam UUID userId) {
        try {
            User user = userService.findUserById(userId).orElseThrow(
                    () -> new IllegalArgumentException("User not found"));
            return ResponseEntity.ok(spotifyArtistStatsService.getArtistStatsByTimeRange(user, SpotifyTimeRange.MEDIUM_TERM));
        } catch (Exception e) {
            log.error("Error fetching medium-term artists", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/long-term")
    public ResponseEntity<?> getLongTermArtists(@RequestParam UUID userId) {
        try {
            User user = userService.findUserById(userId).orElseThrow(
                    () -> new IllegalArgumentException("User not found"));
            return ResponseEntity.ok(spotifyArtistStatsService.getArtistStatsByTimeRange(user, SpotifyTimeRange.LONG_TERM));
        } catch (Exception e) {
            log.error("Error fetching long-term artists", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/refresh/short-term")
    public ResponseEntity<?> refreshShortTermStats(@RequestParam UUID userId) {
        try {
            User user = userService.findUserById(userId).orElseThrow(
                    () -> new IllegalArgumentException("User not found"));
            spotifyArtistStatsService.updateStatsByTimeRange(user, SpotifyTimeRange.SHORT_TERM);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error refreshing short-term stats", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/refresh/medium-term")
    public ResponseEntity<?> refreshMediumTermStats(@RequestParam UUID userId) {
        try {
            User user = userService.findUserById(userId).orElseThrow(
                    () -> new IllegalArgumentException("User not found"));
            spotifyArtistStatsService.updateStatsByTimeRange(user, SpotifyTimeRange.MEDIUM_TERM);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error refreshing medium-term stats", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/refresh/long-term")
    public ResponseEntity<?> refreshLongTermStats(@RequestParam UUID userId) {
        try {
            User user = userService.findUserById(userId).orElseThrow(
                    () -> new IllegalArgumentException("User not found"));
            spotifyArtistStatsService.updateStatsByTimeRange(user, SpotifyTimeRange.LONG_TERM);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error refreshing long-term stats", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/refresh/all")
    public ResponseEntity<?> refreshAllStats(@RequestParam UUID userId) {
        try {
            User user = userService.findUserById(userId).orElseThrow(
                    () -> new IllegalArgumentException("User not found"));
            spotifyArtistStatsService.updateAllStats(user);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error refreshing all stats", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
} 