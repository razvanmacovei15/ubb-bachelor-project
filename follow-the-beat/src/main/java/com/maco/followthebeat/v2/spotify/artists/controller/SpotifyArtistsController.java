package com.maco.followthebeat.v2.spotify.artists.controller;

import com.maco.followthebeat.v2.cache.RedisStateCacheServiceImpl;
import com.maco.followthebeat.v2.spotify.artists.dto.SpotifyArtistDto;
import com.maco.followthebeat.v2.user.context.IsConnected;
import com.maco.followthebeat.v2.user.context.UserContext;
import com.maco.followthebeat.v2.user.entity.User;
import com.maco.followthebeat.v2.spotify.enums.SpotifyTimeRange;
import com.maco.followthebeat.v2.user.service.impl.ListeningProfileOrchestrationService;
import com.maco.followthebeat.v2.user.service.interfaces.UserGenreFrequencyService;
import com.maco.followthebeat.v2.user.service.interfaces.UserService;
import com.maco.followthebeat.v2.spotify.artists.service.interfaces.SpotifyArtistStatsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/spotify-artists")
@RequiredArgsConstructor
public class SpotifyArtistsController {
    private final SpotifyArtistStatsService spotifyArtistStatsService;
    private final RedisStateCacheServiceImpl redisStateCacheService;
    private final UserService userService;
    private final UserContext userContext;
    private final ListeningProfileOrchestrationService listeningProfileOrchestrationService;


    @IsConnected
    @GetMapping("/top-artists")
    public ResponseEntity<List<SpotifyArtistDto>> getTopArtists(
            @RequestParam(defaultValue = "medium_term") SpotifyTimeRange range,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0", required = false) int offset) {

        User user = userContext.getOrThrow();
        log.info("Fetching top artists for user: {}", user.getId());

        List<SpotifyArtistDto> artists;
        if (!user.isActive()) {
            artists = spotifyArtistStatsService.fetchAndSaveInitialStats(user, range);

            listeningProfileOrchestrationService.enrichUserProfileWithGenres(user);
        } else {
            artists = spotifyArtistStatsService.getTopArtistsByTimeRange(user, range);
        }
        return ResponseEntity.ok(artists);
    }

    @IsConnected
    @PostMapping(value = "/refresh", produces = "application/json")
    public ResponseEntity<Void> refreshStats(
            @RequestParam(defaultValue = "medium_term") String range) {

        User user = userContext.getOrThrow();

        SpotifyTimeRange timeRange = SpotifyTimeRange.valueOf(range);

        spotifyArtistStatsService.updateStatsByTimeRange(user, timeRange);

        return ResponseEntity.ok().build();
    }

}