package com.maco.followthebeat.v2.spotify.tracks.entity;

import com.maco.followthebeat.v2.spotify.artists.entity.DbSpotifyArtist;
import com.maco.followthebeat.v2.spotify.tracks.entity.DbSpotifyTrack;
import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;

@Entity
@Table(name = "track_artist")
@Data
public class TrackArtist {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "track_id", nullable = false)
    private DbSpotifyTrack track;

    @ManyToOne
    @JoinColumn(name = "artist_id", nullable = false)
    private DbSpotifyArtist artist;

    @Column(name = "artist_order", nullable = false)
    private Integer artistOrder;
} 