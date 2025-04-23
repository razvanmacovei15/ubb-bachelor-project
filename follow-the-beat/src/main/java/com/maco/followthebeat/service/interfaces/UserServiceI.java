package com.maco.followthebeat.service.interfaces;

import com.maco.followthebeat.entity.User;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface UserServiceI {
    UUID createUser(User user);
    UUID createAnonymousUser();
    void deleteUser(UUID userId);
    void updateUser(UUID userId);
    User getUser(UUID userId);
    User getUserBySpotifyId(String spotifyId);
    User mergeAnonymousUser(User currentUser, User existingUser);
}
