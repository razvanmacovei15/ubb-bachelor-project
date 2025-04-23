package com.maco.followthebeat.service;

import com.maco.followthebeat.entity.User;
import com.maco.followthebeat.repo.UserRepo;
import com.maco.followthebeat.service.interfaces.UserServiceI;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements UserServiceI {
    private final UserRepo userRepo;

    @Override
    public UUID createUser(User user) {
        return null;
    }

    @Override
    public UUID createAnonymousUser() {
        User user = new User();
        user.setAnonymous(true);
        userRepo.save(user);
        return user.getId();
    }

    @Override
    public void deleteUser(UUID userId) {

    }

    @Override
    public void updateUser(UUID userId) {

    }

    @Override
    public User getUser(UUID userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));
        return user;
    }

    @Override
    public User getUserBySpotifyId(String spotifyId) {
        return userRepo.findBySpotifyUserId(spotifyId);
    }

    @Override
    public User mergeAnonymousUser(User currentUser, User existingUser) {
        return null;
    }


}
