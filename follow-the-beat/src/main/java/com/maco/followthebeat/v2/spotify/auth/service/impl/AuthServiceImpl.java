package com.maco.followthebeat.v2.spotify.auth.service.impl;

import com.maco.client.v2.SpotifyClient;
import com.maco.followthebeat.v2.cache.RedisStateCacheServiceImpl;
import com.maco.followthebeat.v2.common.exceptions.UserNotFoundException;
import com.maco.followthebeat.v2.core.entity.ConcertCompatibility;
import com.maco.followthebeat.v2.core.service.interfaces.ConcertCompatibilityService;
import com.maco.followthebeat.v2.spotify.auth.client.SpotifyClientManager;
import com.maco.followthebeat.v2.spotify.auth.userdata.entity.SpotifyUserData;
import com.maco.followthebeat.v2.user.context.UserContext;
import com.maco.followthebeat.v2.user.entity.User;
import com.maco.followthebeat.v2.spotify.auth.service.interfaces.AuthService;
import com.maco.followthebeat.v2.spotify.auth.userdata.service.interfaces.SpotifyUserDataService;
import com.maco.followthebeat.v2.user.entity.UserListeningProfile;
import com.maco.followthebeat.v2.user.service.interfaces.UserListeningProfileService;
import com.maco.followthebeat.v2.user.service.interfaces.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final SpotifyUserDataService spotifyUserDataService;
    private final UserListeningProfileService userListeningProfileService;
    private final ConcertCompatibilityService concertCompatibilityService;

    @Override
    @Transactional
    public User linkSpotifyAccountAndStoreListeningProfile(User user, SpotifyClient client) {
        SpotifyUserData sp = spotifyUserDataService.createSpotifyData(client, user);
        user.setSpotifyUserData(sp);
        user.setHasSpotifyConnected(true);
        storeListeningProfile(user);
        concertCompatibilityService.initCompatibilities(user.getId());
        userService.updateUser(user);
        log.info("User {} linked Spotify account and is connected {} and active {}", user.getId(), user.isHasSpotifyConnected(), user.isActive());
        concertCompatibilityService.initCompatibilities(user.getId());
        return user;
    }

    private void storeListeningProfile(User user){
        UserListeningProfile listeningProfile = userListeningProfileService.getOrCreateListeningProfile(user);
        user.setUserListeningProfile(listeningProfile);
    }
}