package com.maco.followthebeat.feature.base.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
@Entity
@Data
@Table(name = "schedules")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    @OneToOne
    @JoinColumn(name = "concert_id")
    private Concert concert;
}
