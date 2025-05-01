package com.maco.followthebeat.feature.base.service.interfaces;

import com.maco.followthebeat.feature.base.entity.Stage;
import com.maco.followthebeat.feature.base.generics.BaseCrudService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StageService extends BaseCrudService<Stage> {
    Optional<Stage> findByNameAndFestivalId(String name, UUID festivalId);
}