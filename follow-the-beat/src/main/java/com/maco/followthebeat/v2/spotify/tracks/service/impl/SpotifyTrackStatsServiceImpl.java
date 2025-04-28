package com.maco.followthebeat.v2.spotify.tracks.service.impl;

import com.maco.followthebeat.v2.common.enums.SpotifyTimeRange;
import com.maco.followthebeat.v2.spotify.api.SpotifyApiTracksService;
import com.maco.followthebeat.v2.spotify.artists.dto.SpotifyArtistDto;
import com.maco.followthebeat.v2.spotify.artists.entity.BaseUserTopArtist;
import com.maco.followthebeat.v2.spotify.artists.strategy.ArtistSaveStrategy;
import com.maco.followthebeat.v2.spotify.tracks.dto.SpotifyTrackDto;
import com.maco.followthebeat.v2.spotify.tracks.entity.BaseUserTopTrack;
import com.maco.followthebeat.v2.spotify.tracks.entity.DbSpotifyTrack;
import com.maco.followthebeat.v2.spotify.tracks.mapper.SpotifyTrackMapper;
import com.maco.followthebeat.v2.spotify.tracks.service.interfaces.SpotifyTrackService;
import com.maco.followthebeat.v2.spotify.tracks.service.interfaces.SpotifyTrackStatsService;
import com.maco.followthebeat.v2.spotify.tracks.strategy.TrackSaveStrategyFactory;
import com.maco.followthebeat.v2.spotify.tracks.strategy.TracksSaveStrategy;
import com.maco.followthebeat.v2.user.entity.User;
import com.maco.followthebeat.v2.user.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SpotifyTrackStatsServiceImpl implements SpotifyTrackStatsService {
    private final UserService userService;
    private final TrackSaveStrategyFactory trackSaveStrategyFactory;
    private final SpotifyApiTracksService spotifyApiTracksService;
    private final SpotifyTrackMapper spotifyTrackMapper;
    private final SpotifyTrackService spotifyTrackService;

    private final ExecutorService executor = Executors.newFixedThreadPool(2); // 2 async threads for background saves

    @Override
    public void updateAllStats(User user) {
        for (SpotifyTimeRange timeRange : SpotifyTimeRange.values()) {
            updateStatsByTimeRange(user, timeRange);
        }
    }

    @Override
    public void deleteAllStats(User user) {
        for (SpotifyTimeRange timeRange : SpotifyTimeRange.values()) {
            TracksSaveStrategy tracksSaveStrategy = trackSaveStrategyFactory.getStrategy(timeRange);
            tracksSaveStrategy.deleteByUser(user);
        }
    }

    @Override
    public List<SpotifyTrackDto> fetchAndSaveInitialStats(User user, SpotifyTimeRange timeRange) {
        // 1. Fetch current time range
        List<SpotifyTrackDto> requestedTracks = spotifyApiTracksService.fetchTopItems(
                user.getId(), timeRange, 50, 0
        );

        // 2. Save current time range
        saveTracksAndStats(user, requestedTracks, timeRange);
        // 2.1. Async fetch and save the other two time ranges
        CompletableFuture.runAsync(() -> fetchAndSaveOtherTimeRanges(user, timeRange), executor);
        // 3. Optionally mark user active
        userService.setIsActive(true, user);

        // 4. Return fetched tracks
        return requestedTracks;
    }

    private void fetchAndSaveOtherTimeRanges(User user, SpotifyTimeRange alreadyFetched) {
        for (SpotifyTimeRange timeRange : SpotifyTimeRange.values()) {
            if (!timeRange.equals(alreadyFetched)) {
                try {
                    List<SpotifyTrackDto> tracks = spotifyApiTracksService.fetchTopItems(
                            user.getId(), timeRange, 50, 0
                    );
                    saveTracksAndStats(user, tracks, timeRange);
                } catch (Exception e) {
                    log.error("Failed to fetch/save artists for time range: " + timeRange, e);
                }
            }
        }
    }

    private void saveTracksAndStats(User user, List<SpotifyTrackDto> tracks, SpotifyTimeRange timeRange) {
        TracksSaveStrategy tracksSaveStrategy = trackSaveStrategyFactory.getStrategy(timeRange);

        tracksSaveStrategy.deleteByUser(user);

        for (int i = 0; i < tracks.size(); i++) {
            SpotifyTrackDto clientTrack = tracks.get(i);
            DbSpotifyTrack dbSpotifyTrack = spotifyTrackMapper.toDbSpotifyTrack(clientTrack);
            DbSpotifyTrack managedTrack = spotifyTrackService.saveOrGetExistingTrack(dbSpotifyTrack);

            tracksSaveStrategy.saveTrack(user, managedTrack, i + 1);

        }
    }

    @Override
    public List<? extends BaseUserTopTrack> getTrackStatsByTimeRange(User user, SpotifyTimeRange timeRange) {
        TracksSaveStrategy tracksSaveStrategy = trackSaveStrategyFactory.getStrategy(timeRange);
        return tracksSaveStrategy.findAllByUser(user);
    }

    @Override
    public void updateStatsByTimeRange(User user, SpotifyTimeRange timeRange) {
        TracksSaveStrategy tracksSaveStrategy = trackSaveStrategyFactory.getStrategy(timeRange);
        tracksSaveStrategy.updateStats(user);
    }

    @Override
    public List<SpotifyTrackDto> getTopTracksByTimeRange(User user, SpotifyTimeRange timeRange) {
        List<? extends BaseUserTopTrack> tracks = getTrackStatsByTimeRange(user, timeRange);

        return tracks.stream()
                .map(spotifyTrackMapper::fromDbSpotifyTrack)
                .toList();
    }
}
