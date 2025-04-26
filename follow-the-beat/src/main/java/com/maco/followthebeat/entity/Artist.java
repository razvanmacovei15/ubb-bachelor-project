package com.maco.followthebeat.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "artists")
@Data
@NoArgsConstructor
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column
    private String imageKey;

    @ElementCollection
    @CollectionTable(name = "artist_genres", joinColumns = @JoinColumn(name = "artist_id"))
    @Column(name = "genre")
    private List<String> genres;

    @OneToOne(mappedBy = "artist", cascade = CascadeType.ALL, orphanRemoval = true)
    private SpotifyArtistData spotifyData;

} 