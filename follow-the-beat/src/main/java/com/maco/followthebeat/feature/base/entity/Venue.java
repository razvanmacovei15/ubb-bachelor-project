package com.maco.followthebeat.feature.base.entity;

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
