package com.maco.followthebeat.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "spotify_track_data")
@Data
@NoArgsConstructor
public class SpotifyTrackData {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "track_id", nullable = false)
    private Track track;

    @Column(nullable = false, unique = true)
    private String spotifyId;

    @Column
    private String uri;

    @Column
    private String spotifyUrl;

    @Column
    private String spotifyAlbumId;

    @Column
    private String spotifyArtistId;

    @Column
    private Boolean isExplicit;

    @Column
    private Integer trackNumber;

    @Column
    private Integer discNumber;
} 