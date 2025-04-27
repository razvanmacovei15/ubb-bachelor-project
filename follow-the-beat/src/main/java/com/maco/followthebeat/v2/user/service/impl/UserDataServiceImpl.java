package com.maco.followthebeat.v2.user.service.impl;

import com.maco.followthebeat.v2.spotify.auth.client.SpotifyClientFactory;
import com.maco.followthebeat.v2.spotify.auth.userdata.service.interfaces.SpotifyUserDataService;
import com.maco.followthebeat.v2.user.service.interfaces.UserDataService;
import com.maco.followthebeat.v2.user.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
@RequiredArgsConstructor
public class UserDataServiceImpl implements UserDataService {
    private final UserService userService;
    private final SpotifyUserDataService spotifyUserDataService;
    private final SpotifyClientFactory spotifyClientFactory;

    @Override
    public void initializeUserData(UUID userId) {

    }
}
