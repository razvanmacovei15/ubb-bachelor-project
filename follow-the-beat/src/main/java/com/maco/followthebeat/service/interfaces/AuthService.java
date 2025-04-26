package com.maco.followthebeat.service.interfaces;

import com.maco.client.v2.SpotifyClientI;
import com.maco.followthebeat.entity.User;

import java.util.UUID;

public interface AuthService {
    UUID ensureValidUser(String state);
    User linkSpotifyAccount(User user, SpotifyClientI spotifyClient);
}
