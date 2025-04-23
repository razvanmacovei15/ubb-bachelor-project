package com.maco.followthebeat.controller;

import com.maco.followthebeat.enums.SpotifyTimeRange;
import com.maco.spotify.api.client.SpotifyClient;
import com.maco.spotify.api.model.SpotifyArtist;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/spotify-artists")
public class SpotifyArtistsController {
    private final SpotifyClient spotifyClient;

    @Autowired
    public SpotifyArtistsController(SpotifyClient spotifyClient) {
        this.spotifyClient = spotifyClient;
    }

    @GetMapping("/top-artists")
    public ResponseEntity<List<SpotifyArtist>> getTopArtists(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0", required = false) int offset,
            @RequestParam(defaultValue = "ALL_TIME") SpotifyTimeRange range) {

        return fetchTopArtists(limit, offset, range);
    }

    private ResponseEntity<List<SpotifyArtist>> fetchTopArtists(int limit, int offset, SpotifyTimeRange range) {
        try {
            List<SpotifyArtist> topArtists;
            switch (range) {
                case LAST_6_MONTHS -> topArtists = spotifyClient.getTopArtistsLast6Months(limit, offset);
                case LAST_4_WEEKS -> topArtists = spotifyClient.getTopArtistsLast4Weeks(limit, offset);
                case ALL_TIME -> topArtists = spotifyClient.getTopArtistsAllTime(limit, offset);
                default -> throw new IllegalArgumentException("Unsupported time range: " + range);
            }

            log.info("Successfully retrieved {} top artists for {}", topArtists.size(), range);
            return ResponseEntity.ok(topArtists);

        } catch (IllegalStateException e) {
            log.warn("Spotify client not authenticated: {}", e.getMessage());
            return ResponseEntity.status(401).build();

        } catch (Exception e) {
            log.error("Failed to get top artists for {}: {}", range, e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }
}
