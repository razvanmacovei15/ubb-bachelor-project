package com.maco.followthebeat.v2.user.service.impl;

import com.maco.followthebeat.v2.user.entity.User;
import com.maco.followthebeat.v2.user.entity.UserListeningProfile;
import com.maco.followthebeat.v2.user.repo.UserListeningProfileRepo;
import com.maco.followthebeat.v2.user.service.interfaces.UserGenreFrequencyService;
import com.maco.followthebeat.v2.user.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class ListeningProfileOrchestrationService {

    private final UserListeningProfileRepo profileRepo;
    private final UserGenreFrequencyService userGenreFrequencyService;

    @Async
    @Transactional
    public void enrichUserProfileWithGenres(User user) {
        UserListeningProfile profile = profileRepo.findByUserId(user.getId())
                .orElseThrow();

        userGenreFrequencyService.generateAndSave(profile);
    }
}