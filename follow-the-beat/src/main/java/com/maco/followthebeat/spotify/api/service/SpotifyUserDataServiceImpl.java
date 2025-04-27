package com.maco.followthebeat.spotify.api.service;

import com.maco.client.v2.SpotifyClientI;
import com.maco.followthebeat.entity.SpotifyUserData;
import com.maco.followthebeat.entity.User;
import com.maco.followthebeat.mapper.TokenMapper;
import com.maco.followthebeat.spotify.api.client.SpotifyClientFactory;
import com.maco.followthebeat.spotify.api.repository.SpotifyUserDataRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
@Service
@AllArgsConstructor
public class SpotifyUserDataServiceImpl implements SpotifyUserDataService {
    private final SpotifyUserDataRepo spotifyUserDataRepo;
    private final TokenMapper tokenMapper;

    @Override
    public void createSpotifyData(SpotifyUserData spotifyUserData) {
        spotifyUserDataRepo.save(spotifyUserData);
    }

    @Override
    public SpotifyUserData createSpotifyData(SpotifyClientI spotifyClient, User user) {
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
        return spotifyUserDataRepo.findByUserId(userId);
    }

    @Override
    public Optional<SpotifyUserData> getSpotifyDataByUser(User user) {
        return spotifyUserDataRepo.getSpotifyDataByUser(user);
    }

    @Override
    public boolean isSpotifyDataPresent(UUID userId) {
        Optional<SpotifyUserData> maybeSpotifyData = spotifyUserDataRepo.findByUserId(userId);
        return maybeSpotifyData.isPresent();
    }
}
