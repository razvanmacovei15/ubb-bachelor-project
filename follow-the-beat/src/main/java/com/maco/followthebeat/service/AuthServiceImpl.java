package com.maco.followthebeat.service;

import com.maco.client.v2.SpotifyClientI;
import com.maco.followthebeat.entity.SpotifyUserData;
import com.maco.followthebeat.entity.User;
import com.maco.followthebeat.service.interfaces.AuthService;
import com.maco.followthebeat.spotify.service.SpotifyUserDataService;
import com.maco.followthebeat.service.interfaces.UserService;
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
    public UUID ensureValidUser(String state) {
        UUID userId;
        try {
            userId = UUID.fromString(state);
            Optional<User> maybeExisting = userService.findUserById(userId);
            if (maybeExisting.isPresent()) {
                return userId;
            } else {
                return userService.createAnonymousUser();
            }

        } catch (IllegalArgumentException e) {
            return userService.createAnonymousUser();
        }
    }

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
        } else {
            SpotifyUserData sp = spotifyUserDataService.createSpotifyData(client, user);
            user.setSpotifyUserData(sp);
            userService.updateUser(user);
            return user;
        }
    }

}
