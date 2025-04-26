package com.maco.followthebeat.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tracks")
@Data
@NoArgsConstructor
public class Track {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;

    @Column
    private String album;

    @Column
    private Integer durationMs;

    @Column
    private String imageKey;

    @ElementCollection
    @CollectionTable(name = "track_genres", joinColumns = @JoinColumn(name = "track_id"))
    @Column(name = "genre")
    private List<String> genres;

    @OneToOne(mappedBy = "track", cascade = CascadeType.ALL, orphanRemoval = true)
    private SpotifyTrackData spotifyData;

}