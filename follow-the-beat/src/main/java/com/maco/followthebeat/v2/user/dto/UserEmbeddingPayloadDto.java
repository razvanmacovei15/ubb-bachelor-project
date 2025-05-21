package com.maco.followthebeat.v2.user.dto;

import com.maco.followthebeat.v2.core.dto.ConcertForSuggestionDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEmbeddingPayloadDto {
    private UUID requestId;
    private List<String> topUserArtists;
    private List<ConcertForSuggestionDto> festivalArtists;
    private Map<String, Integer> genreFrequencies;

}
