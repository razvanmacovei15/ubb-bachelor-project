package com.maco.followthebeat.v2.core.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LineupEntryDTO {
    private UUID id;
    @NotNull
    private UUID concertId;

    private String notes;

    private Integer priority;

}
