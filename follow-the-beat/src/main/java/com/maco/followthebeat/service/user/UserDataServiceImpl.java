package com.maco.followthebeat.service.user;

import com.maco.followthebeat.spotify.client.SpotifyClientFactory;
import com.maco.followthebeat.spotify.service.SpotifyDataService;
import com.maco.followthebeat.service.interfaces.UserDataService;
import com.maco.followthebeat.service.interfaces.UserService;
import com.maco.spotify.api.client.SpotifyClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
@RequiredArgsConstructor
public class UserDataServiceImpl implements UserDataService {
    private final UserService userService;
    private final SpotifyDataService spotifyDataService;
    private final SpotifyClientFactory spotifyClientFactory;

    @Override
    public void initializeUserData(UUID userId) {

    }
}
