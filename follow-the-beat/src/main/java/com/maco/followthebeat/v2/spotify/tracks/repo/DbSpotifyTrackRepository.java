package com.maco.followthebeat.v2.spotify.tracks.repo;

import com.maco.followthebeat.v2.spotify.tracks.entity.DbSpotifyTrack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DbSpotifyTrackRepository extends JpaRepository<DbSpotifyTrack, UUID> {
    Optional<DbSpotifyTrack> findBySpotifyId(String spotifyId);
} 