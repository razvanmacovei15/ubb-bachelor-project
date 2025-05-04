package com.maco.followthebeat.v2.core.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
@Entity
@Data
@Table(
        name = "festivals",
        indexes = {
                @Index(name = "idx_festival_name", columnList = "name"),
                @Index(name = "idx_festival_is_active", columnList = "isActive")
        }
)
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
    private Set<Stage> stages = new HashSet<>();
}
