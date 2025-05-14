package com.maco.followthebeat.v2.user.entity;

import com.maco.followthebeat.v2.spotify.artists.entity.LongTermArtist;
import com.maco.followthebeat.v2.spotify.artists.entity.MediumTermArtist;
import com.maco.followthebeat.v2.spotify.artists.entity.ShortTermArtist;
import com.maco.followthebeat.v2.spotify.tracks.entity.LongTermTrack;
import com.maco.followthebeat.v2.spotify.tracks.entity.MediumTermTrack;
import com.maco.followthebeat.v2.spotify.tracks.entity.ShortTermTrack;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "user_listening_profiles")
@Data
public class UserListeningProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL)
    private List<ShortTermArtist> shortTermArtists = new ArrayList<>();

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL)
    private List<MediumTermArtist> mediumTermArtists = new ArrayList<>();

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL)
    private List<LongTermArtist> longTermArtists = new ArrayList<>();

    // Same for tracks...
    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL)
    private List<ShortTermTrack> shortTermTracks = new ArrayList<>();

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL)
    private List<MediumTermTrack> mediumTermTracks = new ArrayList<>();

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL)
    private List<LongTermTrack> longTermTracks = new ArrayList<>();

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL)
    private List<UserGenreFrequency> genres = new ArrayList<>();
}
