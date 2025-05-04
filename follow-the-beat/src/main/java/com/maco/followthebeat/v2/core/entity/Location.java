package com.maco.followthebeat.v2.core.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;
@Entity
@Data
@Table(
        name = "locations",
        indexes = {
                @Index(name = "idx_location_name", columnList = "name")
        }
)
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
