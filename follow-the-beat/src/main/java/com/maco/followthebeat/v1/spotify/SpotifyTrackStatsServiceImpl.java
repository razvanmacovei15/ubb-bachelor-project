package com.maco.followthebeat.v1.spotify;

import com.maco.followthebeat.v2.spotify.tracks.entity.DbSpotifyTrack;
import com.maco.followthebeat.v2.spotify.tracks.entity.LongTermTrack;
import com.maco.followthebeat.v2.spotify.tracks.entity.MediumTermTrack;
import com.maco.followthebeat.v2.spotify.tracks.entity.ShortTermTrack;
import com.maco.followthebeat.v2.spotify.tracks.repo.LongTermTrackRepository;
import com.maco.followthebeat.v2.spotify.tracks.repo.MediumTermTrackRepository;
import com.maco.followthebeat.v2.spotify.tracks.repo.ShortTermTrackRepository;
import com.maco.followthebeat.v2.user.entity.User;
import com.maco.followthebeat.v2.common.enums.SpotifyTimeRange;
import com.maco.followthebeat.v1.spotify.interfaces.SpotifyTrackService;
import com.maco.followthebeat.v1.spotify.interfaces.SpotifyTrackStatsService;
import com.maco.followthebeat.v1.tracks.SpotifyTracksService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional
public class SpotifyTrackStatsServiceImpl implements SpotifyTrackStatsService {
    private final SpotifyTracksService spotifyTracksService;
    private final ShortTermTrackRepository shortTermTrackRepository;
    private final MediumTermTrackRepository mediumTermTrackRepository;
    private final LongTermTrackRepository longTermTrackRepository;
    private final SpotifyTrackService spotifyTrackService;

    @Override
    public List<ShortTermTrack> getShortTermTracks(User user) {
        List<ShortTermTrack> existingStats = shortTermTrackRepository.findAllByUserOrderByRank(user);
        if (existingStats.isEmpty()) {
            updateShortTermStats(user);
            return shortTermTrackRepository.findAllByUserOrderByRank(user);
        }
        return existingStats;
    }

    @Override
    public void updateShortTermStats(User user) {
        ResponseEntity<?> response = spotifyTracksService.fetchTopTracks(
            user.getId(), 
            SpotifyTimeRange.SHORT_TERM, 
            50,
            0
        );

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() instanceof List<?> tracks) {
            // Delete existing stats
            shortTermTrackRepository.deleteByUser(user);
            
            // Save new stats
            IntStream.range(0, tracks.size())
                .forEach(index -> {
                    DbSpotifyTrack dbSpotifyTrack = (DbSpotifyTrack) tracks.get(index);
                    ShortTermTrack shortTermTrack = new ShortTermTrack();
                    shortTermTrack.setUser(user);
                    shortTermTrack.setTrack(dbSpotifyTrack);
                    shortTermTrack.setRank(index + 1);
                    shortTermTrackRepository.save(shortTermTrack);
                });
        }
    }

    @Override
    public List<MediumTermTrack> getMediumTermTracks(User user) {
        List<MediumTermTrack> existingStats = mediumTermTrackRepository.findAllByUserOrderByRank(user);
        if (existingStats.isEmpty()) {
            updateMediumTermStats(user);
            return mediumTermTrackRepository.findAllByUserOrderByRank(user);
        }
        return existingStats;
    }

    @Override
    public void updateMediumTermStats(User user) {
        ResponseEntity<?> response = spotifyTracksService.fetchTopTracks(
            user.getId(), 
            SpotifyTimeRange.MEDIUM_TERM, 
            50,
            0
        );

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() instanceof List<?> tracks) {
            // Delete existing stats
            mediumTermTrackRepository.deleteByUser(user);
            
            // Save new stats
            IntStream.range(0, tracks.size())
                .forEach(index -> {
                    DbSpotifyTrack dbSpotifyTrack = (DbSpotifyTrack) tracks.get(index);
                    MediumTermTrack mediumTermTrack = new MediumTermTrack();
                    mediumTermTrack.setUser(user);
                    mediumTermTrack.setTrack(dbSpotifyTrack);
                    mediumTermTrack.setRank(index + 1);
                    mediumTermTrackRepository.save(mediumTermTrack);
                });
        }
    }

    @Override
    public List<LongTermTrack> getLongTermTracks(User user) {
        List<LongTermTrack> existingStats = longTermTrackRepository.findAllByUserOrderByRank(user);
        if (existingStats.isEmpty()) {
            updateLongTermStats(user);
            return longTermTrackRepository.findAllByUserOrderByRank(user);
        }
        return existingStats;
    }

    @Override
    public void updateLongTermStats(User user) {
        ResponseEntity<?> response = spotifyTracksService.fetchTopTracks(
            user.getId(), 
            SpotifyTimeRange.LONG_TERM, 
            50,
            0
        );

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() instanceof List<?> tracks) {
            // Delete existing stats
            longTermTrackRepository.deleteByUser(user);
            
            // Save new stats
            IntStream.range(0, tracks.size())
                .forEach(index -> {
                    DbSpotifyTrack dbSpotifyTrack = (DbSpotifyTrack) tracks.get(index);
                    LongTermTrack longTermTrack = new LongTermTrack();
                    longTermTrack.setUser(user);
                    longTermTrack.setTrack(dbSpotifyTrack);
                    longTermTrack.setRank(index + 1);
                    longTermTrackRepository.save(longTermTrack);
                });
        }
    }

    @Override
    public void updateAllStats(User user) {
        updateShortTermStats(user);
        updateMediumTermStats(user);
        updateLongTermStats(user);
    }

    @Override
    public void deleteAllStats(User user) {
        shortTermTrackRepository.deleteByUser(user);
        mediumTermTrackRepository.deleteByUser(user);
        longTermTrackRepository.deleteByUser(user);
    }

    @Override
    @Transactional
    public void fetchAndSaveInitialStats(User user) {
        // Fetch and save short term stats
        ResponseEntity<?> shortTermResponse = spotifyTracksService.fetchTopTracks(
            user.getId(), 
            SpotifyTimeRange.SHORT_TERM, 
            50,
            0
        );
        if (shortTermResponse.getStatusCode().is2xxSuccessful() && shortTermResponse.getBody() instanceof List<?> tracks) {
            saveTracksAndStats(user, tracks, SpotifyTimeRange.SHORT_TERM);
        }

        // Fetch and save medium term stats
        ResponseEntity<?> mediumTermResponse = spotifyTracksService.fetchTopTracks(
            user.getId(), 
            SpotifyTimeRange.MEDIUM_TERM, 
            50,
            0
        );
        if (mediumTermResponse.getStatusCode().is2xxSuccessful() && mediumTermResponse.getBody() instanceof List<?> tracks) {
            saveTracksAndStats(user, tracks, SpotifyTimeRange.MEDIUM_TERM);
        }

        // Fetch and save long term stats
        ResponseEntity<?> longTermResponse = spotifyTracksService.fetchTopTracks(
            user.getId(), 
            SpotifyTimeRange.LONG_TERM, 
            50,
            0
        );
        if (longTermResponse.getStatusCode().is2xxSuccessful() && longTermResponse.getBody() instanceof List<?> tracks) {
            saveTracksAndStats(user, tracks, SpotifyTimeRange.LONG_TERM);
        }
    }

    private void saveTracksAndStats(User user, List<?> tracks, SpotifyTimeRange timeRange) {
        // Delete existing stats for this time range
        switch (timeRange) {
            case SHORT_TERM -> shortTermTrackRepository.deleteByUser(user);
            case MEDIUM_TERM -> mediumTermTrackRepository.deleteByUser(user);
            case LONG_TERM -> longTermTrackRepository.deleteByUser(user);
        }

        // Save new stats
        IntStream.range(0, tracks.size())
            .forEach(index -> {
                DbSpotifyTrack dbSpotifyTrack = (DbSpotifyTrack) tracks.get(index);
                
                // First save the Spotify track if it doesn't exist
                spotifyTrackService.saveTrack(dbSpotifyTrack);
                
                // Then create and save the term-based stats
                switch (timeRange) {
                    case SHORT_TERM -> {
                        ShortTermTrack shortTermTrack = new ShortTermTrack();
                        shortTermTrack.setUser(user);
                        shortTermTrack.setTrack(dbSpotifyTrack);
                        shortTermTrack.setRank(index + 1);
                        shortTermTrackRepository.save(shortTermTrack);
                    }
                    case MEDIUM_TERM -> {
                        MediumTermTrack mediumTermTrack = new MediumTermTrack();
                        mediumTermTrack.setUser(user);
                        mediumTermTrack.setTrack(dbSpotifyTrack);
                        mediumTermTrack.setRank(index + 1);
                        mediumTermTrackRepository.save(mediumTermTrack);

                    }
                    case LONG_TERM -> {
                        LongTermTrack longTermTrack = new LongTermTrack();
                        longTermTrack.setUser(user);
                        longTermTrack.setTrack(dbSpotifyTrack);
                        longTermTrack.setRank(index + 1);
                        longTermTrackRepository.save(longTermTrack);
                    }
                }
            });
    }
} 