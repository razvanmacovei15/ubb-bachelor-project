package com.maco.followthebeat.v2.spotify.tracks.service.impl;

import com.maco.followthebeat.v2.spotify.tracks.entity.DbSpotifyTrack;
import com.maco.followthebeat.v2.spotify.tracks.repo.DbSpotifyTrackRepository;
import com.maco.followthebeat.v2.spotify.tracks.service.interfaces.SpotifyTrackService;
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
    public DbSpotifyTrack saveOrGetExistingTrack(DbSpotifyTrack track) {
        return dbSpotifyTrackRepository.findBySpotifyId(track.getSpotifyId())
                .orElseGet(() -> dbSpotifyTrackRepository.save(track));
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
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void syncMultipleTracks(List<String> spotifyIds) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
