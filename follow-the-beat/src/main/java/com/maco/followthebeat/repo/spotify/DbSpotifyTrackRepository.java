package com.maco.followthebeat.repo.spotify;

import com.maco.followthebeat.entity.spotify.DbSpotifyTrack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DbSpotifyTrackRepository extends JpaRepository<DbSpotifyTrack, UUID> {
    Optional<DbSpotifyTrack> findBySpotifyId(String spotifyId);
} 