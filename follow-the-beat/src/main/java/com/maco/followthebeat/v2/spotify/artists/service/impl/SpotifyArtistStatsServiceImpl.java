package com.maco.followthebeat.v2.spotify.artists.service.impl;

import com.maco.followthebeat.v2.spotify.api.SpotifyApiArtistsService;
import com.maco.followthebeat.v2.spotify.artists.dto.SpotifyArtistDto;
import com.maco.followthebeat.v2.user.entity.User;
import com.maco.followthebeat.v2.spotify.artists.entity.DbSpotifyArtist;
import com.maco.followthebeat.v2.spotify.artists.entity.BaseUserTopArtist;
import com.maco.followthebeat.v2.spotify.enums.SpotifyTimeRange;
import com.maco.followthebeat.v2.spotify.artists.mapper.SpotifyArtistMapper;
import com.maco.followthebeat.v2.spotify.artists.strategy.ArtistSaveStrategy;
import com.maco.followthebeat.v2.spotify.artists.strategy.ArtistSaveStrategyFactory;
import com.maco.followthebeat.v2.spotify.artists.service.interfaces.SpotifyArtistService;
import com.maco.followthebeat.v2.spotify.artists.service.interfaces.SpotifyArtistStatsService;
import com.maco.followthebeat.v2.user.service.interfaces.UserGenreFrequencyService;
import com.maco.followthebeat.v2.user.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SpotifyArtistStatsServiceImpl implements SpotifyArtistStatsService{

    private final SpotifyApiArtistsService spotifyApiArtistsService;
    private final SpotifyArtistService spotifyArtistService;
    private final SpotifyArtistMapper spotifyArtistMapper;
    private final UserService userService;
    private final ArtistSaveStrategyFactory artistSaveStrategyFactory;

    private final ExecutorService executor = Executors.newFixedThreadPool(2); // 2 async threads for background saves

    private void saveArtistAndStats(User user, List<SpotifyArtistDto> artists, SpotifyTimeRange timeRange) {
        ArtistSaveStrategy artistSaveStrategy = artistSaveStrategyFactory.getStrategy(timeRange);

        artistSaveStrategy.deleteByUser(user);

        IntStream.range(0, artists.size())
                .forEach(index -> {
                    SpotifyArtistDto clientArtist = artists.get(index);
                    DbSpotifyArtist dbSpotifyArtist = spotifyArtistMapper.dtoToEntity(clientArtist);
                    DbSpotifyArtist managedSpotifyArtist = spotifyArtistService.saveOrGetExistingArtist(dbSpotifyArtist);

                    artistSaveStrategy.saveArtist(user, managedSpotifyArtist, index + 1);
                });
    }

    @Override
    public void updateAllStats(User user) {
        updateStatsByTimeRange(user, SpotifyTimeRange.SHORT_TERM);
        updateStatsByTimeRange(user, SpotifyTimeRange.MEDIUM_TERM);
        updateStatsByTimeRange(user, SpotifyTimeRange.LONG_TERM);
    }

    @Override
    public void deleteAllStats(User user) {
        for (SpotifyTimeRange timeRange : SpotifyTimeRange.values()) {
            ArtistSaveStrategy artistSaveStrategy = artistSaveStrategyFactory.getStrategy(timeRange);
            artistSaveStrategy.deleteByUser(user);
        }
    }

    @Override
    @Transactional
    public List<SpotifyArtistDto> fetchAndSaveInitialStats(User user, SpotifyTimeRange requestedTimeRange) {
        // Step 1: Fetch only the requested time range
        List<SpotifyArtistDto> requestedArtists = spotifyApiArtistsService.fetchTopItems(
                user.getId(), requestedTimeRange, 50, 0
        );

        // Step 2: Save the requested time range immediately (synchronous save)
        saveArtistAndStats(user, requestedArtists, requestedTimeRange);

        // Step 3: Async fetch and save the other two time ranges
        CompletableFuture.runAsync(() -> fetchAndSaveOtherTimeRanges(user, requestedTimeRange), executor);

        // Step 4: Mark user active (after initial save)
        userService.setIsActive(true, user);

        // Step 5: Return the initial requested artists immediately
        return requestedArtists;
    }

    private void fetchAndSaveOtherTimeRanges(User user, SpotifyTimeRange alreadyFetched) {
        for (SpotifyTimeRange timeRange : SpotifyTimeRange.values()) {
            if (!timeRange.equals(alreadyFetched)) {
                try {
                    List<SpotifyArtistDto> artists = spotifyApiArtistsService.fetchTopItems(
                            user.getId(), timeRange, 50, 0
                    );
                    saveArtistAndStats(user, artists, timeRange);
                } catch (Exception e) {
                    log.error("Failed to fetch/save artists for time range: " + timeRange, e);
                }
            }
        }
    }

    @Override
    public List<? extends BaseUserTopArtist> getArtistStatsByTimeRange(User user, SpotifyTimeRange timeRange) {
        ArtistSaveStrategy artistSaveStrategy = artistSaveStrategyFactory.getStrategy(timeRange);
        return artistSaveStrategy.findAllByUser(user);
    }

    @Override
    public void updateStatsByTimeRange(User user, SpotifyTimeRange timeRange) {
        ArtistSaveStrategy artistSaveStrategy = artistSaveStrategyFactory.getStrategy(timeRange);
        artistSaveStrategy.updateStats(user);
    }

    @Override
    public List<SpotifyArtistDto> getTopArtistsByTimeRange(User user, SpotifyTimeRange timeRange) {
        List<? extends BaseUserTopArtist> artists = getArtistStatsByTimeRange(user, timeRange);
        return artists.stream()
                .map(spotifyArtistMapper::mapSpecificEntityToDto)
                .toList();
    }


}
