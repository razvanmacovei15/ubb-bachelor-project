package com.maco.followthebeat.v2.spotify.api;

import com.maco.followthebeat.v2.spotify.enums.SpotifyTimeRange;
import com.maco.followthebeat.v2.spotify.auth.client.SpotifyClientManager;
import com.maco.followthebeat.v2.user.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public abstract class SpotifyApiService<T> {

    protected final SpotifyClientManager clientManager;
    protected final UserService userService;

    public void validateUserAndAuthentication(UUID userId) {
        userService.validateUserExistenceAndAuthentication(userId, clientManager);
    }

    protected abstract List<T> fetchTopItems(UUID userId, SpotifyTimeRange range, int limit, int offset);
}
