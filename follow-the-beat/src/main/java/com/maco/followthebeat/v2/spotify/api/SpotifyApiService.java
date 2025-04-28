package com.maco.followthebeat.v2.spotify.api;

import com.maco.client.v2.SpotifyClientI;
import com.maco.followthebeat.v2.common.enums.SpotifyTimeRange;
import com.maco.followthebeat.v2.common.exceptions.SpotifyAuthenticationException;
import com.maco.followthebeat.v2.common.exceptions.UserNotFoundException;
import com.maco.followthebeat.v2.spotify.artists.mapper.SpotifyArtistMapper;
import com.maco.followthebeat.v2.spotify.auth.client.SpotifyClientManager;
import com.maco.followthebeat.v2.user.service.interfaces.UserService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public abstract class SpotifyApiService<T> {
    private final SpotifyClientManager clientManager;
    private final UserService userService;
    private final SpotifyArtistMapper spotifyArtistMapper;

    public void validateUserAndAuthentication(UUID userId) {
        boolean valid = false;
        if (userService != null) {
            valid = userService.validateUserAndSpotifyAuth(userId, clientManager);
        }
        if (!valid) {
            SpotifyClientI client = null;
            if (clientManager != null) {
                client = clientManager.getOrCreateSpotifyClient(userId);
            }
            if (client != null && !client.isAuthenticated()) {
                throw new SpotifyAuthenticationException("User is not authenticated with Spotify.");
            }
            throw new UserNotFoundException("User not found with id: " + userId);
        }
    }

    protected abstract List<T> fetchTopItems(UUID userId, SpotifyTimeRange range, int limit, int offset) ;

}
