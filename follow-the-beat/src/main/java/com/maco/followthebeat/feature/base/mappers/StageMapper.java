package com.maco.followthebeat.feature.base.mappers;

import com.maco.followthebeat.feature.base.dto.StageDTO;
import com.maco.followthebeat.feature.base.entity.Stage;
import org.springframework.stereotype.Component;

@Component

public class StageMapper {
    public Stage toEntity(StageDTO stageDTO) {
        return Stage.builder()
                .name(stageDTO.getName())
                .build();
    }
}
