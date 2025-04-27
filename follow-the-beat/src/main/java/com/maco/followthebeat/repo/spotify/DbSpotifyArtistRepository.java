package com.maco.followthebeat.repo.spotify;

import com.maco.followthebeat.entity.spotify.DbSpotifyArtist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DbSpotifyArtistRepository extends JpaRepository<DbSpotifyArtist, UUID> {
    Optional<DbSpotifyArtist> findBySpotifyId(String spotifyId);
} 