package com.maco.followthebeat.v2.core.repo;

import com.maco.followthebeat.v2.core.entity.FestivalUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FestivalUserRepo extends JpaRepository<FestivalUser, UUID> {
    boolean existsByFestivalIdAndUserId(UUID festivalId, UUID userId);
    Optional<FestivalUser> findByFestivalIdAndUserId(UUID festivalId, UUID userId);
}
