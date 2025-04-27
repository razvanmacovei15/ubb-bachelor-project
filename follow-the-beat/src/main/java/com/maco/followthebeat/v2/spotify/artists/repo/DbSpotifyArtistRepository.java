package com.maco.followthebeat.v2.spotify.artists.repo;

import com.maco.followthebeat.v2.spotify.artists.entity.DbSpotifyArtist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DbSpotifyArtistRepository extends JpaRepository<DbSpotifyArtist, UUID> {
    Optional<DbSpotifyArtist> findBySpotifyId(String spotifyId);
} 