package com.maco.followthebeat.v2.spotify.auth.service.impl;

import com.maco.client.v2.SpotifyClientI;
import com.maco.followthebeat.v2.cache.RedisStateCacheServiceImpl;
import com.maco.followthebeat.v2.common.exceptions.UserNotFoundException;
import com.maco.followthebeat.v2.spotify.auth.client.SpotifyClientManager;
import com.maco.followthebeat.v2.spotify.auth.userdata.entity.SpotifyUserData;
import com.maco.followthebeat.v2.user.context.UserContext;
import com.maco.followthebeat.v2.user.entity.User;
import com.maco.followthebeat.v2.spotify.auth.service.interfaces.AuthService;
import com.maco.followthebeat.v2.spotify.auth.userdata.service.interfaces.SpotifyUserDataService;
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

    @Override
    @Transactional
    public User linkSpotifyAccount(User user, SpotifyClientI client) {
        String spotifyUserId = client.getCurrentUserDetails().getId();
        Optional<User> maybeExisting = userService.findUserBySpotifyId(spotifyUserId);

        if (maybeExisting.isPresent()) {
            if (user.isAnonymous()) {
                userService.mergeAnonymousUser(user, maybeExisting.get());
            }
            return maybeExisting.get();
        }

        SpotifyUserData sp = spotifyUserDataService.createSpotifyData(client, user);
        user.setSpotifyUserData(sp);
        user.setHasSpotifyConnected(true);
        userService.updateUser(user);
        return user;
    }
}