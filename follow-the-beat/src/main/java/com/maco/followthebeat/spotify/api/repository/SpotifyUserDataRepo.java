package com.maco.followthebeat.spotify.api.repository;

import com.maco.followthebeat.entity.SpotifyUserData;
import com.maco.followthebeat.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpotifyUserDataRepo extends JpaRepository<SpotifyUserData, UUID> {
    Optional<SpotifyUserData> getSpotifyDataByUser(User user);
    Optional<SpotifyUserData> findByUserId(UUID userId);
}
