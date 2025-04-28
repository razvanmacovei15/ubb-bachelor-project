package com.maco.followthebeat.v2.spotify.api;

import com.maco.client.v2.SpotifyClientI;
import com.maco.client.v2.model.SpotifyArtist;
import com.maco.followthebeat.v2.common.enums.SpotifyTimeRange;
import com.maco.followthebeat.v2.common.exceptions.SpotifyAuthenticationException;
import com.maco.followthebeat.v2.common.exceptions.UserNotFoundException;
import com.maco.followthebeat.v2.spotify.artists.dto.SpotifyArtistDto;
import com.maco.followthebeat.v2.spotify.artists.mapper.SpotifyArtistMapper;
import com.maco.followthebeat.v2.user.service.interfaces.UserService;
import com.maco.followthebeat.v2.spotify.auth.client.SpotifyClientManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class SpotifyApiArtistsService extends SpotifyApiService<SpotifyArtistDto> {
    private final SpotifyArtistMapper spotifyArtistMapper;

    public SpotifyApiArtistsService(SpotifyClientManager clientManager, UserService userService, SpotifyArtistMapper spotifyArtistMapper) {
        super(clientManager, userService);
        this.spotifyArtistMapper = spotifyArtistMapper;
    }

    public List<SpotifyArtistDto> fetchTopItems(UUID userId, SpotifyTimeRange range, int limit, int offset) {
        validateUserAndAuthentication(userId);

        SpotifyClientI client = clientManager.getOrCreateSpotifyClient(userId);
        List<SpotifyArtist> tracks = getTopItemsByRange(client, range, limit, offset);
        log.info("Fetched {} tracks from Spotify for user {} and range {}", tracks.size(), userId, range);

        return tracks.stream()
                .map(spotifyArtistMapper::clientToDto)
                .toList();
    }

    protected List<SpotifyArtist> getTopItemsByRange(SpotifyClientI client, SpotifyTimeRange range, int limit, int offset) {
        return switch (range) {
            case SHORT_TERM -> client.getTopArtistsLast4Weeks(limit, offset);
            case MEDIUM_TERM -> client.getTopArtistsLast6Months(limit, offset);
            case LONG_TERM -> client.getTopArtistsAllTime(limit, offset);
            default -> throw new IllegalArgumentException("Unsupported time range: " + range);
        };
    }
}
