package com.maco.followthebeat.entity.spotify.interfaces;

import com.maco.followthebeat.v2.user.entity.User;
import com.maco.followthebeat.entity.spotify.DbSpotifyTrack;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@MappedSuperclass
@Data
public abstract class BaseUserTopTrack {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "track_id", nullable = false)
    private DbSpotifyTrack track;

    @Column(nullable = false)
    private Integer rank;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;
}
