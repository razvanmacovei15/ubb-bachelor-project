package com.maco.followthebeat.v2.user.dto;

import com.maco.followthebeat.v2.core.dto.ArtistForSuggestionDto;
import com.maco.followthebeat.v2.core.entity.Artist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEmbeddingPayloadDto {
    private List<String> topUserArtists;
    private List<ArtistForSuggestionDto> festivalArtists;
    private Map<String, Integer> genreFrequencies;

}
