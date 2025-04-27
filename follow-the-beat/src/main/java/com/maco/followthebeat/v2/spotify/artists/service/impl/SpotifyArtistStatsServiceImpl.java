package com.maco.followthebeat.v2.spotify.artists.service.impl;

import com.maco.client.v2.model.SpotifyArtist;
import com.maco.followthebeat.v2.spotify.api.SpotifyApiArtistsService;
import com.maco.followthebeat.v2.spotify.artists.dto.SpotifyArtistDto;
import com.maco.followthebeat.v2.user.entity.User;
import com.maco.followthebeat.v2.spotify.artists.entity.DbSpotifyArtist;
import com.maco.followthebeat.v2.spotify.artists.entity.BaseUserTopArtist;
import com.maco.followthebeat.v2.common.enums.SpotifyTimeRange;
import com.maco.followthebeat.v2.spotify.artists.mapper.SpotifyArtistMapper;
import com.maco.followthebeat.v2.spotify.artists.repo.LongTermArtistRepository;
import com.maco.followthebeat.v2.spotify.artists.repo.MediumTermArtistRepository;
import com.maco.followthebeat.v2.spotify.artists.repo.ShortTermArtistRepository;
import com.maco.followthebeat.v2.spotify.artists.strategy.ArtistSaveStrategy;
import com.maco.followthebeat.v2.spotify.artists.strategy.ArtistSaveStrategyFactory;
import com.maco.followthebeat.v2.spotify.artists.service.interfaces.SpotifyArtistService;
import com.maco.followthebeat.v2.spotify.artists.service.interfaces.SpotifyArtistStatsService;
import com.maco.followthebeat.v2.user.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SpotifyArtistStatsServiceImpl implements SpotifyArtistStatsService {

    private final SpotifyApiArtistsService spotifyApiArtistsService;
    private final ShortTermArtistRepository shortTermArtistRepository;
    private final MediumTermArtistRepository mediumTermArtistRepository;
    private final LongTermArtistRepository longTermArtistRepository;
    private final SpotifyArtistService spotifyArtistService;
    private final SpotifyArtistMapper spotifyArtistMapper;
    private final UserService userService;
    private final ArtistSaveStrategyFactory artistSaveStrategyFactory;

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
        shortTermArtistRepository.deleteByUser(user);
        mediumTermArtistRepository.deleteByUser(user);
        longTermArtistRepository.deleteByUser(user);
    }

    @Override
    @Transactional
    public List<SpotifyArtistDto> fetchAndSaveInitialStats(User user, SpotifyTimeRange timeRange) {
        List<SpotifyArtistDto> shortTermArtists = spotifyApiArtistsService.fetchTopArtists(
                user.getId(), SpotifyTimeRange.SHORT_TERM, 50, 0
        );
        saveArtistAndStats(user, shortTermArtists, SpotifyTimeRange.SHORT_TERM);

        List<SpotifyArtistDto> mediumTermArtists = spotifyApiArtistsService.fetchTopArtists(
                user.getId(), SpotifyTimeRange.MEDIUM_TERM, 50, 0
        );
        saveArtistAndStats(user, mediumTermArtists, SpotifyTimeRange.MEDIUM_TERM);

        List<SpotifyArtistDto> longTermArtists = spotifyApiArtistsService.fetchTopArtists(
                user.getId(), SpotifyTimeRange.LONG_TERM, 50, 0
        );
        saveArtistAndStats(user, longTermArtists, SpotifyTimeRange.LONG_TERM);
        userService.setIsActive(true, user);
        return getTopArtistsByTimeRange(user, timeRange);
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
