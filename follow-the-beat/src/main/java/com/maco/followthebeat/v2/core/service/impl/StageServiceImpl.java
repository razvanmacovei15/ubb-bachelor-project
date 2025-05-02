package com.maco.followthebeat.v2.core.service.impl;

import com.maco.followthebeat.v2.core.entity.Stage;
import com.maco.followthebeat.v2.core.generics.BaseCrudServiceImpl;
import com.maco.followthebeat.v2.core.repo.StageRepo;
import com.maco.followthebeat.v2.core.service.interfaces.StageService;
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
        return stageRepo.findByNameAndFestivalId(name, festivalId);
    }
}
