package com.maco.followthebeat.feature.base.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleDTO {
    private UUID id;
    private LocalDate date;
    private LocalTime startTime;
    private ConcertDTO concertDTO;
}