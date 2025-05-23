package com.maco.followthebeat.v2.core.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "schedules",
        indexes = {
                @Index(name = "idx_schedule_concert", columnList = "concert_id"),
                @Index(name = "idx_schedule_date", columnList = "date")
        }
)
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private LocalDate date;
    private LocalTime startTime;

    @OneToOne
    @JoinColumn(name = "concert_id")
    private Concert concert;
}
