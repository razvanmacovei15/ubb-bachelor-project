package com.maco.followthebeat.feature.base.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "venues")
public class Venue {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column
    private String address;

    @Column
    private String city;

    @Column
    private String country;

    @Column
    private Integer capacity;

    @OneToMany(mappedBy = "venue")
    private List<Event> events;
}
