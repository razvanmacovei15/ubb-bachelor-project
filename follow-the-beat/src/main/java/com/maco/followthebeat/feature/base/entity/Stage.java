package com.maco.followthebeat.feature.base.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "stages")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "id")
public class Stage  extends Location {

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "festival_id")
    private Festival festival;

    @OneToMany(mappedBy = "location")
    private List<Concert> concerts;
}
