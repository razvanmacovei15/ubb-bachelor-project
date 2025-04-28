package com.maco.followthebeat.v2.spotify.artists.strategy;

import com.maco.followthebeat.v2.common.enums.SpotifyTimeRange;
import com.maco.followthebeat.v2.spotify.artists.strategy.ArtistSaveStrategy;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArtistSaveStrategyFactory {
    private final List<ArtistSaveStrategy> strategies;

    private Map<SpotifyTimeRange, ArtistSaveStrategy> strategyMap;

    @PostConstruct
    public void init() {
        strategyMap = strategies.stream()
                .collect(Collectors.toMap(
                        ArtistSaveStrategy::getTimeRange,
                        strategy -> strategy
                ));
    }

    public ArtistSaveStrategy getStrategy(SpotifyTimeRange timeRange) {
        ArtistSaveStrategy strategy = strategyMap.get(timeRange);
        if (strategy == null) {
            throw new IllegalArgumentException("No strategy found for time range: " + timeRange);
        }
        return strategy;
    }
}
