package com.maco.followthebeat.service;

import com.maco.followthebeat.dto.CreateUserRequest;
import com.maco.followthebeat.entity.User;
import com.maco.followthebeat.repo.UserRepo;
import com.maco.followthebeat.service.interfaces.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    //todo create a future strategy pattern for user creation and other user related actions in accordance with the user type

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
        userRepo.delete(currentUser);
    }

    @Override
    public boolean existsById(UUID userId) {
        return userRepo.existsById(userId);
    }
}
