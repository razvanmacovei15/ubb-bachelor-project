package com.maco.followthebeat.v2.user.service.interfaces;

import com.maco.followthebeat.v2.user.dto.UpdateUserRequest;
import com.maco.followthebeat.v2.user.entity.User;
import com.maco.followthebeat.v2.spotify.auth.client.SpotifyClientManager;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User createUser(UpdateUserRequest userRequest);
    UUID createAnonymousUser();
    void deleteUser(UUID userId);
    void updateUser(User user);
    void updateUser(UpdateUserRequest userRequest, User user);
    Optional<User> findUserById(UUID userId);
    Optional<User> findUserBySpotifyId(String spotifyId);
    void mergeAnonymousUser(User currentUser, User existingUser);
    boolean hasConnectedSpotifyAccount(UUID userId);
    boolean validateUserAndSpotifyAuth(UUID userId, SpotifyClientManager clientManager);
    void setIsActive(boolean bool, User user);
    void validateUserExistenceAndAuthentication(UUID userId, SpotifyClientManager clientManager);
}
