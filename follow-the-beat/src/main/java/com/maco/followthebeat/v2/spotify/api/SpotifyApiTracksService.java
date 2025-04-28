package com.maco.followthebeat.v2.spotify.api;

import com.maco.client.v2.SpotifyClientI;
import com.maco.client.v2.model.SpotifyArtist;
import com.maco.client.v2.model.SpotifyTrack;
import com.maco.followthebeat.v2.common.enums.SpotifyTimeRange;
import com.maco.followthebeat.v2.spotify.auth.client.SpotifyClientManager;
import com.maco.followthebeat.v2.spotify.tracks.dto.SpotifyTrackDto;
import com.maco.followthebeat.v2.spotify.tracks.mapper.SpotifyTrackMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class SpotifyApiTracksService extends SpotifyApiService<SpotifyTrackDto>{
    private final SpotifyClientManager clientManager;
    private final SpotifyTrackMapper spotifyTrackMapper;

    @Override
    protected List<SpotifyTrackDto> fetchTopItems(UUID userId, SpotifyTimeRange range, int limit, int offset) {
        validateUserAndAuthentication(userId);

        SpotifyClientI client = clientManager.getOrCreateSpotifyClient(userId);
        List<SpotifyTrack> artists = getTopItemsByRange(client, range, limit, offset);
        return artists.stream()
                .map(spotifyTrackMapper::fromSpotifyTrack)
                .toList();
    }

    protected List<SpotifyTrack> getTopItemsByRange(SpotifyClientI client, SpotifyTimeRange range, int limit, int offset) {
        return switch (range) {
            case SHORT_TERM -> client.getTopTracksLast4Weeks(limit, offset);
            case MEDIUM_TERM -> client.getTopTracksLast6Months(limit, offset);
            case LONG_TERM -> client.getTopTracksAllTime(limit, offset);
            default -> throw new IllegalArgumentException("Unsupported time range: " + range);
        };
    }
}
