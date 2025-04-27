package com.maco.followthebeat.v2.spotify.auth.userdata.service.interfaces;

import com.maco.client.v2.SpotifyClientI;
import com.maco.followthebeat.v2.spotify.auth.userdata.entity.SpotifyUserData;
import com.maco.followthebeat.v2.user.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface SpotifyUserDataService {
    void createSpotifyData(SpotifyUserData spotifyUserData);
    SpotifyUserData createSpotifyData(SpotifyClientI spotifyClient, User user);
    void deleteSpotifyData(UUID userId);
    void updateSpotifyData(SpotifyUserData spotifyUserData);
    Optional<SpotifyUserData> getSpotifyData(UUID userId);
    Optional<SpotifyUserData> getSpotifyDataByUser(User user);
    boolean isSpotifyDataPresent(UUID userId);
}
