package com.maco.followthebeat.service;

import com.maco.followthebeat.entity.SpotifyPlatform;
import com.maco.followthebeat.repo.SpotifyPlatformRepo;
import com.maco.followthebeat.service.interfaces.SpotifyPlatformServiceI;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
@AllArgsConstructor
public class SpotifyPlatformService implements SpotifyPlatformServiceI {
    private final SpotifyPlatformRepo spotifyPlatformRepo;

    @Override
    public void createSpotifyPlatform(SpotifyPlatform spotifyPlatform) {
        spotifyPlatformRepo.save(spotifyPlatform);
    }

    @Override
    public void deleteSpotifyPlatform(UUID userId) {

    }

    @Override
    public void updateSpotifyPlatform(SpotifyPlatform spotifyPlatform) {

    }

    @Override
    public String getSpotifyPlatform(UUID userId) {
        return "";
    }
}
