package com.maco.followthebeat.v2.user.repo;

import com.maco.followthebeat.v2.user.entity.UserGenreFrequency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserGenreFrequencyRepo extends JpaRepository<UserGenreFrequency, UUID> {
    UserGenreFrequency findByUserId(UUID userId);
}
