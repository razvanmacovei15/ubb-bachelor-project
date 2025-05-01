package com.maco.followthebeat.feature.base.mappers;

import com.maco.followthebeat.feature.base.dto.ScheduleDTO;
import com.maco.followthebeat.feature.base.entity.Schedule;
import org.springframework.stereotype.Component;

@Component

public class ScheduleMapper {
    public Schedule toEntity(ScheduleDTO scheduleDTO) {
        return Schedule.builder()
                .date(scheduleDTO.getDate())
                .startTime(scheduleDTO.getStartTime())
                .build();
    }
}
