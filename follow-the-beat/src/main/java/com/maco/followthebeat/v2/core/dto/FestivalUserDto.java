package com.maco.followthebeat.v2.core.dto;

import com.maco.followthebeat.v2.spotify.enums.SpotifyTimeRange;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FestivalUserDto {
    private UUID id;
    private UUID festivalId;
    private UUID userId;
    private Boolean generatedCompatibility;
    private SpotifyTimeRange timeRange;
}
