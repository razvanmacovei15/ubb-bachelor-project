package com.maco.followthebeat.spotify.service;

import com.maco.followthebeat.entity.SpotifyData;
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
public class SpotifyPlatformServiceImpl implements com.maco.followthebeat.service.interfaces.SpotifyPlatformService {
    private final SpotifyDataRepo spotifyDataRepo;
    private final TokenMapper tokenMapper;
    private final SpotifyClientFactory spotifyClientFactory;

    @Override
    public void createSpotifyPlatform(SpotifyData spotifyData) {
        spotifyDataRepo.save(spotifyData);
    }

    @Override
    public SpotifyData createSpotifyPlatform(SpotifyClient spotifyClient, User user) {
        return tokenMapper.toEntity(spotifyClient, user);

    }

    @Override
    public void deleteSpotifyPlatform(UUID userId) {

    }

    @Override
    public void updateSpotifyPlatform(SpotifyData spotifyData) {

    }

    @Override
    public Optional<SpotifyData> getSpotifyPlatform(UUID userId) {
        return null;
    }

    @Override
    public Optional<SpotifyData> getSpotifyPlatformByUser(User user) {
        return spotifyDataRepo.getSpotifyPlatformByUser(user);
    }


}
