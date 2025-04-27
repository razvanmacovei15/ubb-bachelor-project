package com.maco.followthebeat.service.spotify;

import com.maco.client.v2.model.SpotifyArtist;
import com.maco.followthebeat.entity.User;
import com.maco.followthebeat.entity.spotify.ShortTermArtist;
import com.maco.followthebeat.entity.spotify.MediumTermArtist;
import com.maco.followthebeat.entity.spotify.LongTermArtist;
import com.maco.followthebeat.entity.spotify.DbSpotifyArtist;
import com.maco.followthebeat.entity.spotify.interfaces.BaseUserTopArtist;
import com.maco.followthebeat.enums.SpotifyTimeRange;
import com.maco.followthebeat.mapper.SpotifyArtistMapper;
import com.maco.followthebeat.repo.spotify.LongTermArtistRepository;
import com.maco.followthebeat.repo.spotify.MediumTermArtistRepository;
import com.maco.followthebeat.repo.spotify.ShortTermArtistRepository;
import com.maco.followthebeat.service.savestrategy.ArtistSaveStrategy;
import com.maco.followthebeat.service.savestrategy.ArtistSaveStrategyFactory;
import com.maco.followthebeat.service.spotify.interfaces.SpotifyArtistService;
import com.maco.followthebeat.service.spotify.interfaces.SpotifyArtistStatsService;
import com.maco.followthebeat.spotify.api.service.artists.SpotifyApiArtistsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
    private final ArtistSaveStrategyFactory artistSaveStrategyFactory;

    private void saveArtistAndStats(User user, List<?> artists, SpotifyTimeRange timeRange){
        ArtistSaveStrategy artistSaveStrategy = artistSaveStrategyFactory.getStrategy(timeRange);

        artistSaveStrategy.deleteByUser(user);

        IntStream.range(0, artists.size())
            .forEach(index -> {
                SpotifyArtist clientArtist = (SpotifyArtist) artists.get(index);
                DbSpotifyArtist dbSpotifyArtist = spotifyArtistMapper.clientToEntity(clientArtist);
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
    public void fetchAndSaveInitialStats(User user) {
        ResponseEntity<?> shortTermResponse = spotifyApiArtistsService.fetchTopArtists(
                user.getId(),
                SpotifyTimeRange.SHORT_TERM,
                50,
                0
        );
        if (shortTermResponse.getStatusCode().is2xxSuccessful() && shortTermResponse.getBody() instanceof List<?> artists) {
            saveArtistAndStats(user, artists, SpotifyTimeRange.SHORT_TERM);
        }

        ResponseEntity<?> mediumTermResponse = spotifyApiArtistsService.fetchTopArtists(
                user.getId(),
                SpotifyTimeRange.MEDIUM_TERM,
                50,
                0
        );
        if (mediumTermResponse.getStatusCode().is2xxSuccessful() && mediumTermResponse.getBody() instanceof List<?> artists) {
            saveArtistAndStats(user, artists, SpotifyTimeRange.MEDIUM_TERM);
        }

        ResponseEntity<?> longTermResponse = spotifyApiArtistsService.fetchTopArtists(
                user.getId(),
                SpotifyTimeRange.LONG_TERM,
                50,
                0
        );
        if (longTermResponse.getStatusCode().is2xxSuccessful() && longTermResponse.getBody() instanceof List<?> artists) {
            saveArtistAndStats(user, artists, SpotifyTimeRange.LONG_TERM);
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
} 