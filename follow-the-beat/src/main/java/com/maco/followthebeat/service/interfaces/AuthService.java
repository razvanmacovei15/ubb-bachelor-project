package com.maco.followthebeat.service.interfaces;

import com.maco.followthebeat.entity.User;
import com.maco.spotify.api.client.SpotifyClient;

import java.util.UUID;

public interface AuthService {
    UUID ensureValidUser(String state);
    User linkSpotifyAccount(User user, SpotifyClient spotifyClient);
}
