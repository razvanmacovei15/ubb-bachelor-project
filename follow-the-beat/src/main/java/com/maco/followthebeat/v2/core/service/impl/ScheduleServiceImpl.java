package com.maco.followthebeat.v2.core.service.impl;

import com.maco.followthebeat.v2.core.entity.Schedule;
import com.maco.followthebeat.v2.core.generics.BaseCrudServiceImpl;
import com.maco.followthebeat.v2.core.repo.ScheduleRepo;
import com.maco.followthebeat.v2.core.service.interfaces.ScheduleService;
import org.springframework.stereotype.Service;

@Service
public class ScheduleServiceImpl extends BaseCrudServiceImpl<Schedule> implements ScheduleService {
    public ScheduleServiceImpl(ScheduleRepo scheduleRepo) {
        super(scheduleRepo);
    }
}
