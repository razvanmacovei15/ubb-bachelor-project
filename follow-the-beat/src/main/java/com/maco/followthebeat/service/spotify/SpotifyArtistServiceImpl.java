package com.maco.followthebeat.service.spotify;

import com.maco.followthebeat.entity.spotify.DbSpotifyArtist;
import com.maco.followthebeat.repo.spotify.DbSpotifyArtistRepository;
import com.maco.followthebeat.service.spotify.interfaces.SpotifyArtistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class SpotifyArtistServiceImpl implements SpotifyArtistService {
    private final DbSpotifyArtistRepository dbSpotifyArtistRepository;

    @Override
    public Optional<DbSpotifyArtist> getArtist(String spotifyId) {
        return dbSpotifyArtistRepository.findBySpotifyId(spotifyId);
    }

    @Override
    public DbSpotifyArtist saveArtist(DbSpotifyArtist artist) {
        return dbSpotifyArtistRepository.save(artist);
    }

    @Override
    public DbSpotifyArtist saveOrGetExistingArtist(DbSpotifyArtist artist) {
        return dbSpotifyArtistRepository.findBySpotifyId(artist.getSpotifyId())
                .orElseGet(() -> dbSpotifyArtistRepository.save(artist));
    }

    @Override
    public List<DbSpotifyArtist> getArtists(List<String> spotifyIds) {
        return spotifyIds.stream()
                .map(dbSpotifyArtistRepository::findBySpotifyId)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    @Override
    public boolean deleteArtist(String spotifyId) {
        Optional<DbSpotifyArtist> artist = dbSpotifyArtistRepository.findBySpotifyId(spotifyId);
        if (artist.isPresent()) {
            dbSpotifyArtistRepository.delete(artist.get());
            return true;
        }
        return false;
    }

    @Override
    public void syncArtistData(String spotifyId) {
        // TODO: Implement using spotifyArtistsService
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void syncMultipleArtists(List<String> spotifyIds) {
        // TODO: Implement using spotifyArtistsService
        throw new UnsupportedOperationException("Not implemented yet");
    }
} 