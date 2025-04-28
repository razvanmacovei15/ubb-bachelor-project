package com.maco.followthebeat.v2.spotify.tracks.strategy;

import com.maco.followthebeat.v2.common.enums.SpotifyTimeRange;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrackSaveStrategyFactory {
    private final List<TracksSaveStrategy> strategies;

    private Map<SpotifyTimeRange, TracksSaveStrategy> strategyMap;

    @PostConstruct
    public void init() {
        strategyMap = strategies.stream()
                .collect(Collectors.toMap(
                        TracksSaveStrategy::getTimeRange,
                        strategy -> strategy
                ));
    }

    public TracksSaveStrategy getStrategy(SpotifyTimeRange timeRange) {
        TracksSaveStrategy strategy = strategyMap.get(timeRange);
        if (strategy == null) {
            throw new IllegalArgumentException("No track save strategy found for time range: " + timeRange);
        }
        return strategy;
    }
}
