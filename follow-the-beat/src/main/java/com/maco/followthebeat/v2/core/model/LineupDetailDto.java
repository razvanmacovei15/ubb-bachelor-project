package com.maco.followthebeat.v2.core.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
public class LineupDetailDto {
    private UUID id;
    private String artistName;
    private String artistImageUrl;
    private String spotifyUrl;
    private String notes;
    private Integer priority;
    private Integer compatibility;
    private LocalTime startTime;
    private LocalDate date;
    private String stageName;
    private String festivalName;

    public LineupDetailDto(UUID id, String artistName, String artistImageUrl, String spotifyUrl,
                           String notes, Integer priority, Integer compatibility,
                           LocalTime startTime, LocalDate date, String stageName, String festivalName) {
        this.id = id;
        this.artistName = artistName;
        this.artistImageUrl = artistImageUrl;
        this.spotifyUrl = spotifyUrl;
        this.notes = notes;
        this.priority = priority;
        this.compatibility = compatibility;
        this.startTime = startTime;
        this.date = date;
        this.stageName = stageName;
        this.festivalName = festivalName;
    }
}
