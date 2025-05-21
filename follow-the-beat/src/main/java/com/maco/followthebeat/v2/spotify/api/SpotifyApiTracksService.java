package com.maco.followthebeat.v2.spotify.api;


import com.maco.client.v2.SpotifyClient;
import com.maco.client.v2.model.SpotifyTrack;
import com.maco.followthebeat.v2.spotify.enums.SpotifyTimeRange;
import com.maco.followthebeat.v2.spotify.auth.client.SpotifyClientManager;
import com.maco.followthebeat.v2.spotify.tracks.dto.SpotifyTrackDto;
import com.maco.followthebeat.v2.spotify.tracks.mapper.SpotifyTrackMapper;
import com.maco.followthebeat.v2.user.service.interfaces.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
@Service
public class SpotifyApiTracksService extends SpotifyApiService<SpotifyTrackDto>{
    private final SpotifyClientManager clientManager;
    private final SpotifyTrackMapper spotifyTrackMapper;

    public SpotifyApiTracksService(SpotifyClientManager clientManager, UserService userService, SpotifyTrackMapper spotifyTrackMapper) {
        super(clientManager, userService);
        this.clientManager = clientManager;
        this.spotifyTrackMapper = spotifyTrackMapper;
    }

    @Override
    public List<SpotifyTrackDto> fetchTopItems(UUID userId, SpotifyTimeRange range, int limit, int offset) {
        validateUserAndAuthentication(userId);

        SpotifyClient client = clientManager.getOrCreateSpotifyClient(userId);
        List<SpotifyTrack> artists = getTopItemsByRange(client, range, limit, offset);
        return artists.stream()
                .map(spotifyTrackMapper::fromSpotifyTrack)
                .toList();
    }

    private List<SpotifyTrack> getTopItemsByRange(SpotifyClient client, SpotifyTimeRange range, int limit, int offset) {
        return switch (range) {
            case SHORT_TERM -> client.getTopTracksLast4Weeks(limit, offset);
            case MEDIUM_TERM -> client.getTopTracksLast6Months(limit, offset);
            case LONG_TERM -> client.getTopTracksAllTime(limit, offset);
            default -> throw new IllegalArgumentException("Unsupported time range: " + range);
        };
    }
}
