package com.maco.followthebeat.v2.spotify.artists.controller;

import com.maco.client.v2.model.SpotifyArtist;
import com.maco.followthebeat.v2.common.exceptions.SpotifyAuthenticationException;
import com.maco.followthebeat.v2.common.exceptions.UserNotFoundException;
import com.maco.followthebeat.v2.spotify.artists.dto.SpotifyArtistDto;
import com.maco.followthebeat.v2.user.entity.User;
import com.maco.followthebeat.v2.spotify.artists.entity.BaseUserTopArtist;
import com.maco.followthebeat.v2.common.enums.SpotifyTimeRange;
import com.maco.followthebeat.v2.spotify.artists.mapper.SpotifyArtistMapper;
import com.maco.followthebeat.v2.user.service.interfaces.UserService;
import com.maco.followthebeat.v2.spotify.artists.service.interfaces.SpotifyArtistStatsService;
import com.maco.followthebeat.v2.spotify.api.SpotifyApiArtistsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
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

    @GetMapping("/top-artists")
    public ResponseEntity<List<SpotifyArtistDto>> getTopArtists(
            @RequestParam(required = false) UUID userId,
            @RequestParam(defaultValue = "medium_term") SpotifyTimeRange range,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0", required = false) int offset) {
        Optional<User> userOptional = userService.findUserById(userId);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(List.of());
        }
        User user = userOptional.get();



        if(!user.isActive()){
            List<SpotifyArtistDto> artists = spotifyArtistStatsService.fetchAndSaveInitialStats(user, range);
            log.info("Fetched and saved initial stats for user: {}, range: {}", user.getId(), range);
            return ResponseEntity.ok(artists);
        }
        List<SpotifyArtistDto> artists = spotifyArtistStatsService.getTopArtistsByTimeRange(user, range);
        log.info("Fetched top artists for user: {}, range: {} from db.", user.getId(), range);
        return ResponseEntity.ok(artists);
    }



    @PostMapping(value = "/refresh", produces = "application/json")
    public ResponseEntity<Void> refreshStats(
            @RequestParam String userId,
            @RequestParam(defaultValue = "medium_term") String range) {

        log.info("Received refresh request for userId: {}, range: {}", userId, range);

        UUID userUuid = UUID.fromString(userId);
        User user = userService.findUserById(userUuid)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        SpotifyTimeRange timeRange = SpotifyTimeRange.valueOf(range);

        spotifyArtistStatsService.updateStatsByTimeRange(user, timeRange);

        return ResponseEntity.ok().build();
    }

}