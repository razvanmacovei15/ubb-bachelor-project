package com.maco.followthebeat.feature.base.service.impl;

import com.maco.followthebeat.feature.base.entity.Stage;
import com.maco.followthebeat.feature.base.generics.BaseCrudServiceImpl;
import com.maco.followthebeat.feature.base.repo.StageRepo;
import com.maco.followthebeat.feature.base.service.interfaces.StageService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class StageServiceImpl extends BaseCrudServiceImpl<Stage> implements StageService {
    private final StageRepo stageRepo;
    public StageServiceImpl(StageRepo stageRepo) {
        super(stageRepo);
        this.stageRepo = stageRepo;
    }


    @Override
    public Optional<Stage> findByNameAndFestivalId(String name, UUID festivalId) {
        return stageRepo.findByNameAndFestival_Id(name, festivalId);
    }
}
