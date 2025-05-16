package com.maco.followthebeat.v2.user.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class UserGenreFrequencyDto {
    private UUID id;
    private UUID userId;
    private String genre;
    private Integer count;
    private UUID profileId;
}
