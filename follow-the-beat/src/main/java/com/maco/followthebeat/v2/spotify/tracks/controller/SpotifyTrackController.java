package com.maco.followthebeat.v2.spotify.tracks.controller;

import com.maco.followthebeat.v2.cache.RedisStateCacheServiceImpl;
import com.maco.followthebeat.v2.spotify.enums.SpotifyTimeRange;
import com.maco.followthebeat.v2.spotify.tracks.dto.SpotifyTrackDto;
import com.maco.followthebeat.v2.spotify.tracks.service.interfaces.SpotifyTrackStatsService;
import com.maco.followthebeat.v2.user.context.IsConnected;
import com.maco.followthebeat.v2.user.context.UserContext;
import com.maco.followthebeat.v2.user.entity.User;
import com.maco.followthebeat.v2.user.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/spotify-tracks")
@RequiredArgsConstructor
public class SpotifyTrackController {
    private final SpotifyTrackStatsService spotifyTrackStatsService;
    private final RedisStateCacheServiceImpl stateCacheService;
    private final UserService userService;
    private final UserContext userContext;

    @IsConnected
    @GetMapping("/top-tracks")
    public ResponseEntity<List<SpotifyTrackDto>> getTopTracks(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "medium_term") SpotifyTimeRange range) {

        User user = userContext.getOrThrow();

        List<SpotifyTrackDto> tracks;
        if (!user.isActive()) {
            tracks = spotifyTrackStatsService.fetchAndSaveInitialStats(user, range);
        } else {
            tracks = spotifyTrackStatsService.getTopTracksByTimeRange(user, range);
        }
        return ResponseEntity.ok(tracks);

    }

    @IsConnected
    @PostMapping("/refresh")
    public ResponseEntity<Void> refreshStats(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(defaultValue = "medium_term") String range) {

        User user = userContext.getOrThrow();

        SpotifyTimeRange timeRange = SpotifyTimeRange.valueOf(range);
        spotifyTrackStatsService.updateStatsByTimeRange(user, timeRange);

        return ResponseEntity.ok().build();
    }

}