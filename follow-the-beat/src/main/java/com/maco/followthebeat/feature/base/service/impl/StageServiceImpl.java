package com.maco.followthebeat.feature.base.service.impl;

import com.maco.followthebeat.feature.base.entity.Stage;
import com.maco.followthebeat.feature.base.generics.BaseCrudServiceImpl;
import com.maco.followthebeat.feature.base.repo.StageRepo;
import com.maco.followthebeat.feature.base.service.interfaces.StageService;
import org.springframework.stereotype.Service;

@Service
public class StageServiceImpl extends BaseCrudServiceImpl<Stage> implements StageService {
    public StageServiceImpl(StageRepo stageRepo) {
        super(stageRepo);
    }
}
