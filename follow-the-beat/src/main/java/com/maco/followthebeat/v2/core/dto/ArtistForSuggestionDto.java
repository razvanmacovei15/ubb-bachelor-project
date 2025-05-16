package com.maco.followthebeat.v2.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArtistForSuggestionDto {
    private String name;
    private List<String> genres;
}
