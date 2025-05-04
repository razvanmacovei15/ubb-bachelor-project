package com.maco.followthebeat.v2.core.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "venues",
        indexes = {
                @Index(name = "idx_venue_city", columnList = "city"),
                @Index(name = "idx_venue_country", columnList = "country")
        }
)
@PrimaryKeyJoinColumn(name = "id")
public class Venue extends Location {

    @Column
    private String address;

    @Column
    private String city;

    @Column
    private String country;

    @Column
    private Integer capacity;

    @OneToMany(mappedBy = "location")
    private List<Concert> concerts;
}
