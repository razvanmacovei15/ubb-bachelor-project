package com.maco.followthebeat.v2.spotify.auth.userdata.entity;

import com.maco.followthebeat.v2.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "spotify_user_data")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpotifyUserData {
    @Id
    @GeneratedValue
    private UUID id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    private User user;
    @Column(name = "spotify_user_id", unique = true, nullable = false)
    private String spotifyUserId;
    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;
    @Column(name = "access_token", nullable = false)
    private String accessToken;
    @Column(name = "expires_in", nullable = false)
    private long expiresIn;
    @Column(name = "token_type", nullable = false)
    private String tokenType;
    @Column(name = "scope", nullable = false)
    private String scope;
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;
}
