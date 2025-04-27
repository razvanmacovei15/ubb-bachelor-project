package com.maco.followthebeat.service.user;

import com.maco.followthebeat.spotify.api.client.SpotifyClientFactory;
import com.maco.followthebeat.spotify.api.service.SpotifyUserDataService;
import com.maco.followthebeat.service.interfaces.UserDataService;
import com.maco.followthebeat.service.interfaces.UserService;
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
