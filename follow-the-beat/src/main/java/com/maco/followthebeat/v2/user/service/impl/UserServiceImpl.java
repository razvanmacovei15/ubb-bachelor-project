package com.maco.followthebeat.v2.user.service.impl;

import com.maco.client.v2.SpotifyClientI;
import com.maco.followthebeat.v2.user.dto.CreateUserRequest;
import com.maco.followthebeat.v2.user.entity.User;
import com.maco.followthebeat.v2.user.repo.UserRepo;
import com.maco.followthebeat.v2.user.service.interfaces.UserService;
import com.maco.followthebeat.v2.spotify.auth.client.SpotifyClientManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;


    @Override
    public User createUser(CreateUserRequest userRequest) {
        if (userRepo.existsByEmail(userRequest.getEmail())) {
            throw new IllegalArgumentException("Email already in use: " + userRequest.getEmail());
        }
        if (userRepo.existsByUsername(userRequest.getUsername())) {
            throw new IllegalArgumentException("Username already in use: " + userRequest.getUsername());
        }

        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setPasswordHash(passwordEncoder.encode(userRequest.getPassword()));
        user.setAnonymous(false);

        return userRepo.save(user);
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
        if(!userRepo.existsById(userId)) {
            throw new EntityNotFoundException("User with id " + userId + " does not exist.");
        }
        userRepo.deleteById(userId);
    }

    @Override
    public void updateUser(User user) {
        userRepo.save(user);
    }

    @Override
    public Optional<User> findUserById(UUID userId) {
        return userRepo.findById(userId);
    }

    @Override
    public Optional<User> findUserBySpotifyId(String spotifyId) {
        return userRepo.findBySpotifyUserId(spotifyId);
    }

    @Override
    public void mergeAnonymousUser(User currentUser, User existingUser) {
        if(currentUser == existingUser){
            return;
        }
        userRepo.delete(currentUser);
    }

    @Override
    public boolean hasConnectedSpotifyAccount(UUID userId) {
        return userRepo.hasSpotifyConnected(userId);
    }

    @Override
    public boolean validateUserAndSpotifyAuth(UUID userId, SpotifyClientManager clientManager) {
        // Check if user exists
        Optional<User> userOptional = findUserById(userId);
        if (userOptional.isEmpty()) {
            return false;
        }

        // Check Spotify authentication
        SpotifyClientI client = clientManager.getOrCreateSpotifyClient(userId);
        return client.isAuthenticated();
    }

    @Override
    public void setIsActive(boolean bool, User user) {
        user.setActive(bool);
        userRepo.save(user);
    }

}
