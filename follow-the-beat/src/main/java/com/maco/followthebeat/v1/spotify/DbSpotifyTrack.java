package com.maco.followthebeat.v1.spotify;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "spotify_track")
@Data
public class DbSpotifyTrack {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "spotify_id", unique = true, nullable = false)
    private String spotifyId;

    @Column(nullable = false)
    private String name;

    @Column(name = "duration_ms")
    private Integer durationMs;

    private Integer popularity;

    @Column(name = "preview_url")
    private String previewUrl;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    @OneToMany(mappedBy = "track")
    private List<TrackArtist> artists;
}