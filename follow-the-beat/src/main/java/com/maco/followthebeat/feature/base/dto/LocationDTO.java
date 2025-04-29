package com.maco.followthebeat.feature.base.dto;

import lombok.*;

import java.util.UUID;
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class LocationDTO {
    private UUID id;
    private String name;
    private String imgUrl;
}
