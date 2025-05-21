package com.maco.followthebeat.v2.spotify.auth.service.interfaces;

import com.maco.client.v2.SpotifyClient;
import com.maco.followthebeat.v2.user.entity.User;

public interface AuthService {
    User linkSpotifyAccountAndStoreListeningProfile(User user, SpotifyClient spotifyClient);
}
