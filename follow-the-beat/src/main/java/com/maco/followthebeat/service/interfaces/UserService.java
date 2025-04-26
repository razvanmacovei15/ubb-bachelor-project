package com.maco.followthebeat.service.interfaces;

import com.maco.client.v2.SpotifyClientI;
import com.maco.followthebeat.dto.CreateUserRequest;
import com.maco.followthebeat.entity.User;
import com.maco.followthebeat.spotify.client.SpotifyClientManager;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User createUser(CreateUserRequest userRequest);
    UUID createAnonymousUser();
    void deleteUser(UUID userId);
    void updateUser(User user);
    Optional<User> findUserById(UUID userId);
    Optional<User> findUserBySpotifyId(String spotifyId);
    void mergeAnonymousUser(User currentUser, User existingUser);
    boolean hasConnectedSpotifyAccount(UUID userId);
    boolean validateUserAndSpotifyAuth(UUID userId, SpotifyClientManager clientManager);
}
