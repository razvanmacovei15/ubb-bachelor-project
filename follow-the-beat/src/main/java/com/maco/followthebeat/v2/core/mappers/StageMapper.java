package com.maco.followthebeat.v2.core.mappers;

import com.maco.followthebeat.v2.core.dto.StageDTO;
import com.maco.followthebeat.v2.core.entity.Stage;
import org.springframework.stereotype.Component;

@Component

public class StageMapper {
    public Stage toEntity(StageDTO stageDTO) {
        Stage stage = new Stage();
        stage.setName(stageDTO.getName());
        return stage;
    }
}
