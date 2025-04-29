package com.maco.followthebeat.feature.base.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
@Data
@Builder
@RequiredArgsConstructor
public class ScheduleDTO {
    private UUID id;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private ConcertDTO concertDTO;
}