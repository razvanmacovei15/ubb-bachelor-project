package com.maco.followthebeat.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "spotify_clients")
@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class SpotifyPlatform {
    @Id
    @GeneratedValue
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
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
}
