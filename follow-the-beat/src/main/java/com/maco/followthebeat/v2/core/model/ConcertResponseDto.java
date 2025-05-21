package com.maco.followthebeat.v2.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class ConcertResponseDto {
    private UUID concertId;
    private String artistName;
    private String artistImageUrl;
    private Float compatibility;
    private LocalTime startTime;
    private LocalDate date;
    private String festivalName;
    private String stageName;
}
