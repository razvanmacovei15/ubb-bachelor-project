package com.maco.followthebeat.v2.spotify.api;

import com.maco.client.v2.SpotifyClient;
import com.maco.client.v2.model.SpotifyArtist;
import com.maco.followthebeat.v2.spotify.enums.SpotifyTimeRange;
import com.maco.followthebeat.v2.spotify.artists.dto.SpotifyArtistDto;
import com.maco.followthebeat.v2.spotify.artists.mapper.SpotifyArtistMapper;
import com.maco.followthebeat.v2.user.service.interfaces.UserService;
import com.maco.followthebeat.v2.spotify.auth.client.SpotifyClientManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

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

        SpotifyClient client = clientManager.getOrCreateSpotifyClient(userId);
        List<SpotifyArtist> artists = getTopItemsByRange(client, range, limit, offset);

        return IntStream.range(0, artists.size())
                .mapToObj(i -> {
                    SpotifyArtistDto dto = spotifyArtistMapper.clientToDto(artists.get(i));
                    dto.setRank(i + offset + 1); // rank should be 1-based and respect pagination offset
                    return dto;
                })
                .toList();
    }


    protected List<SpotifyArtist> getTopItemsByRange(SpotifyClient client, SpotifyTimeRange range, int limit, int offset) {
        return switch (range) {
            case SHORT_TERM -> client.getTopArtistsLast4Weeks(limit, offset);
            case MEDIUM_TERM -> client.getTopArtistsLast6Months(limit, offset);
            case LONG_TERM -> client.getTopArtistsAllTime(limit, offset);
            default -> throw new IllegalArgumentException("Unsupported time range: " + range);
        };
    }
}
