package com.maco.followthebeat.spotify.service;

import com.maco.followthebeat.entity.SpotifyUserData;
import com.maco.followthebeat.entity.User;
import com.maco.followthebeat.mapper.TokenMapper;
import com.maco.followthebeat.spotify.client.SpotifyClientFactory;
import com.maco.followthebeat.spotify.repository.SpotifyDataRepo;
import com.maco.spotify.api.client.SpotifyClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
@Service
@AllArgsConstructor
public class SpotifyDataServiceImpl implements SpotifyDataService {
    private final SpotifyDataRepo spotifyDataRepo;
    private final TokenMapper tokenMapper;
    private final SpotifyClientFactory spotifyClientFactory;

    @Override
    public void createSpotifyData(SpotifyUserData spotifyUserData) {
        spotifyDataRepo.save(spotifyUserData);
    }

    @Override
    public SpotifyUserData createSpotifyData(SpotifyClient spotifyClient, User user) {
        return tokenMapper.toEntity(spotifyClient, user);

    }

    @Override
    public void deleteSpotifyData(UUID userId) {

    }

    @Override
    public void updateSpotifyData(SpotifyUserData spotifyUserData) {

    }

    @Override
    public Optional<SpotifyUserData> getSpotifyData(UUID userId) {
        return spotifyDataRepo.findByUserId(userId);
    }

    @Override
    public Optional<SpotifyUserData> getSpotifyDataByUser(User user) {
        return spotifyDataRepo.getSpotifyDataByUser(user);
    }

    @Override
    public boolean isSpotifyDataPresent(UUID userId) {
        Optional<SpotifyUserData> maybeSpotifyData = spotifyDataRepo.findByUserId(userId);
        return maybeSpotifyData.isPresent();
    }
}
