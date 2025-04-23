package com.maco.followthebeat.controller;

import com.maco.followthebeat.enums.SpotifyTimeRange;
import com.maco.spotify.api.client.SpotifyClient;
import com.maco.spotify.api.model.SpotifyTrack;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//todo create DTOs for artists, tracks and user
//todo create entities for artists, tracks

@Slf4j
@RestController
@RequestMapping("/spotify-tracks")
public class SpotifyTrackController {
    private final SpotifyClient spotifyClient;

    @Autowired
    public SpotifyTrackController(SpotifyClient spotifyClient) {
        this.spotifyClient = spotifyClient;
    }

    @GetMapping("/top-tracks")
    public ResponseEntity<List<SpotifyTrack>> getTopTracks(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0", required = false) int offset,
            @RequestParam(defaultValue = "ALL_TIME") SpotifyTimeRange range) {

        return fetchTopTracks(limit, offset, range);
    }

    private ResponseEntity<List<SpotifyTrack>> fetchTopTracks(int limit, int offset, SpotifyTimeRange range) {
        try {
            List<SpotifyTrack> topTracks;
            switch (range) {
                case LAST_6_MONTHS -> topTracks = spotifyClient.getTopTracksLast6Months(limit, offset);
                case LAST_4_WEEKS -> topTracks = spotifyClient.getTopTracksLast4Weeks(limit, offset);
                case ALL_TIME -> topTracks = spotifyClient.getTopTracksAllTime(limit, offset);
                default -> throw new IllegalArgumentException("Unsupported time range: " + range);
            }

            log.info("Successfully retrieved {} top tracks for {}", topTracks.size(), range);
            return ResponseEntity.ok(topTracks);

        } catch (IllegalStateException e) {
            log.warn("Spotify client not authenticated: {}", e.getMessage());
            return ResponseEntity.status(401).build();

        } catch (Exception e) {
            log.error("Failed to get top tracks for {}: {}", range, e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }


}
