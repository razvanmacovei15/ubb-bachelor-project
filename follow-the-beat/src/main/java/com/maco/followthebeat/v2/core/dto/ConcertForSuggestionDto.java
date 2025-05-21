package com.maco.followthebeat.v2.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConcertForSuggestionDto {
    private UUID concertId;
    private String artistName;
    private List<String> genres;
}
