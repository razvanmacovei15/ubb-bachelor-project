package com.maco.followthebeat.v2.core.mappers;

import com.maco.followthebeat.v2.core.dto.ScheduleDTO;
import com.maco.followthebeat.v2.core.entity.Schedule;
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
