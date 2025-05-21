package com.maco.followthebeat.v2.user.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "user_genre_frequencys")
@Data
public class UserGenreFrequency {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID userId;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    private UserListeningProfile profile;

    @Column(nullable = false)
    private String genre;

    @Column(nullable = false)
    private Integer count;
}
