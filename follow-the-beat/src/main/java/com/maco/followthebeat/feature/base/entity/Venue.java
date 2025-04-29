package com.maco.followthebeat.feature.base.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "venues")
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
