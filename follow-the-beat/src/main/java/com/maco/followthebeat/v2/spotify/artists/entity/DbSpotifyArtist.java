package com.maco.followthebeat.v2.spotify.artists.entity;

import com.maco.followthebeat.entity.spotify.TrackArtist;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "spotify_artist")
@Data
public class DbSpotifyArtist {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "spotify_id", unique = true, nullable = false)
    private String spotifyId;

    @Column(nullable = false)
    private String name;

    @Column(name = "image_url")
    private String imageUrl;

    @ElementCollection
    @CollectionTable(name = "spotify_artist_genres", joinColumns = @JoinColumn(name = "artist_id"))
    @Column(name = "genre")
    private List<String> genres;

    private Integer popularity;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    @OneToMany(mappedBy = "artist")
    private List<TrackArtist> tracks;

} 