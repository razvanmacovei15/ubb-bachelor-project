package com.maco.followthebeat.service.spotify;

import com.maco.followthebeat.entity.spotify.DbSpotifyTrack;
import com.maco.followthebeat.repo.spotify.DbSpotifyTrackRepository;
import com.maco.followthebeat.service.spotify.interfaces.SpotifyTrackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class SpotifyTrackServiceImpl implements SpotifyTrackService {
    private final DbSpotifyTrackRepository dbSpotifyTrackRepository;

    @Override
    public Optional<DbSpotifyTrack> getTrack(String spotifyId) {
        return dbSpotifyTrackRepository.findBySpotifyId(spotifyId);
    }

    @Override
    public DbSpotifyTrack saveTrack(DbSpotifyTrack track) {
        return dbSpotifyTrackRepository.save(track);
    }

    @Override
    public List<DbSpotifyTrack> getTracks(List<String> spotifyIds) {
        return spotifyIds.stream()
                .map(dbSpotifyTrackRepository::findBySpotifyId)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    @Override
    public boolean deleteTrack(String spotifyId) {
        Optional<DbSpotifyTrack> track = dbSpotifyTrackRepository.findBySpotifyId(spotifyId);
        if (track.isPresent()) {
            dbSpotifyTrackRepository.delete(track.get());
            return true;
        }
        return false;
    }

    @Override
    public void syncTrackData(String spotifyId) {
        // TODO: Implement Spotify API call to fetch track data
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void syncMultipleTracks(List<String> spotifyIds) {
        // TODO: Implement batch Spotify API call to fetch multiple tracks
        throw new UnsupportedOperationException("Not implemented yet");
    }
} 