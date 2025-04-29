package com.maco.followthebeat.feature.base.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;
@Data
@Builder
@RequiredArgsConstructor
public abstract class LocationDTO {
    private UUID id;
    private String name;
    private String imgUrl;
}
