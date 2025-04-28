package com.maco.followthebeat.v2.spotify.tracks.controller;

import com.maco.followthebeat.v2.common.enums.SpotifyTimeRange;
import com.maco.followthebeat.v2.spotify.tracks.service.interfaces.SpotifyTrackStatsService;
import com.maco.followthebeat.v2.user.entity.User;
import com.maco.followthebeat.v2.user.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/spotify/tracks/stats")
@RequiredArgsConstructor
public class SpotifyTrackStatsController {
    private final SpotifyTrackStatsService spotifyTrackStatsService;
    private final UserService userService;

    @GetMapping("/short-term")
    public ResponseEntity<?> getShortTermTracks(@RequestParam UUID userId) {
        try {
            User user = userService.findUserById(userId).orElseThrow(
                    () -> new IllegalArgumentException("User not found"));
            return ResponseEntity.ok(spotifyTrackStatsService.getTrackStatsByTimeRange(user, SpotifyTimeRange.SHORT_TERM));
        } catch (Exception e) {
            log.error("Error fetching short-term tracks", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/medium-term")
    public ResponseEntity<?> getMediumTermTracks(@RequestParam UUID userId) {
        try {
            User user = userService.findUserById(userId).orElseThrow(
                    () -> new IllegalArgumentException("User not found"));
            return ResponseEntity.ok(spotifyTrackStatsService.getTopTracksByTimeRange(user, SpotifyTimeRange.MEDIUM_TERM));
        } catch (Exception e) {
            log.error("Error fetching medium-term tracks", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/long-term")
    public ResponseEntity<?> getLongTermTracks(@RequestParam UUID userId) {
        try {
            User user = userService.findUserById(userId).orElseThrow(
                    () -> new IllegalArgumentException("User not found"));
            return ResponseEntity.ok(spotifyTrackStatsService.getTrackStatsByTimeRange(user, SpotifyTimeRange.LONG_TERM));
        } catch (Exception e) {
            log.error("Error fetching long-term tracks", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/refresh/short-term")
    public ResponseEntity<?> refreshShortTermStats(@RequestParam UUID userId) {
        try {
            User user = userService.findUserById(userId).orElseThrow(
                    () -> new IllegalArgumentException("User not found"));
            spotifyTrackStatsService.updateStatsByTimeRange(user, SpotifyTimeRange.SHORT_TERM);
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
            spotifyTrackStatsService.updateStatsByTimeRange(user, SpotifyTimeRange.MEDIUM_TERM);
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
            spotifyTrackStatsService.updateStatsByTimeRange(user, SpotifyTimeRange.LONG_TERM);
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
            spotifyTrackStatsService.updateAllStats(user);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error refreshing all stats", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
} 