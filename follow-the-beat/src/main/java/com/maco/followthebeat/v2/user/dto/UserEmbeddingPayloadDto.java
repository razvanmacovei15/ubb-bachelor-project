package com.maco.followthebeat.v2.user.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;
@Data
public class UserEmbeddingPayloadDto {
    private List<String> topArtists;

    private Map<String, Integer> genreFrequencies;

}
