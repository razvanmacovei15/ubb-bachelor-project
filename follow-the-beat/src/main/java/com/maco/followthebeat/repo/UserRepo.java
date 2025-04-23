package com.maco.followthebeat.repo;

import com.maco.followthebeat.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepo extends JpaRepository<User, UUID> {
    @Query("SELECT u FROM User u JOIN u.spotifyPlatform sp WHERE sp.spotifyUserId = :spotifyUserId")
    Optional<User> findBySpotifyUserId(@Param("spotifyUserId") String spotifyUserId);
    boolean existsById(UUID id);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
