package com.maco.followthebeat.v2.core.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;
@Entity
@Data
@Table(name = "locations")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "location_type")
public abstract class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "img_url")
    private String imgUrl;
}
