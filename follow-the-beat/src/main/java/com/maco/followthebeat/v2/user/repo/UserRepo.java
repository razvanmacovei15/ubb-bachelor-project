package com.maco.followthebeat.v2.user.repo;

import com.maco.followthebeat.v2.user.entity.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepo extends JpaRepository<User, UUID> {
    @Query("SELECT u FROM User u JOIN u.spotifyUserData sp WHERE sp.spotifyUserId = :spotifyUserId")
    Optional<User> findBySpotifyUserId(@Param("spotifyUserId") String spotifyUserId);

    boolean existsById(UUID userId);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    @NotNull
    Optional<User> findById(@NotNull UUID userId);
}
