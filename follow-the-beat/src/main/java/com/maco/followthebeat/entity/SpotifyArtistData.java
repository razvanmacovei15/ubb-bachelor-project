package com.maco.followthebeat.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "spotify_artist_data")
@Data
@NoArgsConstructor
public class SpotifyArtistData {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;

    @Column(nullable = false, unique = true)
    private String spotifyId;

    @Column
    private String uri;

    @Column
    private String spotifyUrl;

    @Column
    private Integer popularity;

    @Column
    private Integer followers;
} 