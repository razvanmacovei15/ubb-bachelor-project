package com.maco.followthebeat.feature.base.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
@Entity
@Data
@Table(name = "festivals")
public class Festival {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    private String description;
    private String location;
    private LocalDate startDate;
    private LocalDate endDate;
    private String logoUrl;
    private String websiteUrl;
    private Boolean isActive;

    @ManyToMany
    @JoinTable(
            name = "festival_artists",
            joinColumns = @JoinColumn(name = "festival_id"),
            inverseJoinColumns = @JoinColumn(name = "artist_id")
    )
    private List<Artist> artists;

    @OneToMany(mappedBy = "festival", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Stage> stages;
}
