package com.maco.followthebeat.service;

import com.maco.followthebeat.entity.SpotifyPlatform;
import com.maco.followthebeat.entity.User;
import com.maco.followthebeat.mapper.TokenMapper;
import com.maco.followthebeat.repo.SpotifyPlatformRepo;
import com.maco.spotify.api.client.SpotifyClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
@Service
@AllArgsConstructor
public class SpotifyPlatformServiceImpl implements com.maco.followthebeat.service.interfaces.SpotifyPlatformService {
    private final SpotifyPlatformRepo spotifyPlatformRepo;
    private final TokenMapper tokenMapper;

    @Override
    public void createSpotifyPlatform(SpotifyPlatform spotifyPlatform) {
        spotifyPlatformRepo.save(spotifyPlatform);
    }

    @Override
    public SpotifyPlatform createSpotifyPlatform(SpotifyClient spotifyClient, User user) {
        return tokenMapper.toEntity(spotifyClient, user);

    }

    @Override
    public void deleteSpotifyPlatform(UUID userId) {

    }

    @Override
    public void updateSpotifyPlatform(SpotifyPlatform spotifyPlatform) {

    }

    @Override
    public Optional<SpotifyPlatform> getSpotifyPlatform(UUID userId) {
        return null;
    }

    @Override
    public Optional<SpotifyPlatform> getSpotifyPlatformByUser(User user) {
        return spotifyPlatformRepo.getSpotifyPlatformByUser(user);
    }


}
