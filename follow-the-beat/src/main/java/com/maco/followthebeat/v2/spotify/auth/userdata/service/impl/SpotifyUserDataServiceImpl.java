package com.maco.followthebeat.v2.spotify.auth.userdata.service.impl;

import com.maco.client.v2.SpotifyClient;
import com.maco.followthebeat.v2.spotify.auth.userdata.service.interfaces.SpotifyUserDataService;
import com.maco.followthebeat.v2.spotify.auth.userdata.entity.SpotifyUserData;
import com.maco.followthebeat.v2.user.entity.User;
import com.maco.followthebeat.v2.spotify.auth.mapper.TokenMapper;
import com.maco.followthebeat.v2.spotify.auth.userdata.repo.SpotifyUserDataRepo;
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
