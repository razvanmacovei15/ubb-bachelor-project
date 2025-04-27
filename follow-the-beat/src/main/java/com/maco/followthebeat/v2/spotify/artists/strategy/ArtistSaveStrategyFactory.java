package com.maco.followthebeat.v2.spotify.artists.strategy;

import com.maco.followthebeat.v2.common.enums.SpotifyTimeRange;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArtistSaveStrategyFactory {
    private final ShortTermArtistSaveStrategy shortTermArtistSaveStrategy;
    private final MediumTermArtistSaveStrategy mediumTermArtistSaveStrategy;
    private final LongTermArtistSaveStrategy longTermArtistSaveStrategy;

    public ArtistSaveStrategy getStrategy(SpotifyTimeRange timeRange) {
        return switch (timeRange) {
            case SHORT_TERM -> shortTermArtistSaveStrategy;
            case MEDIUM_TERM -> mediumTermArtistSaveStrategy;
            case LONG_TERM -> longTermArtistSaveStrategy;
        };
    }
}