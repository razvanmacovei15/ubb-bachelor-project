package com.maco.followthebeat.feature.base.service.impl;

import com.maco.followthebeat.feature.base.entity.Schedule;
import com.maco.followthebeat.feature.base.generics.BaseCrudServiceImpl;
import com.maco.followthebeat.feature.base.repo.ScheduleRepo;
import com.maco.followthebeat.feature.base.service.interfaces.ScheduleService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
public class ScheduleServiceImpl extends BaseCrudServiceImpl<Schedule> implements ScheduleService {
    public ScheduleServiceImpl(ScheduleRepo scheduleRepo) {
        super(scheduleRepo);
    }
}
