package com.maco.followthebeat.v2.core.dto;

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