package com.maco.followthebeat.v2.core.service.interfaces;

import com.maco.followthebeat.v2.core.entity.Stage;
import com.maco.followthebeat.v2.core.generics.BaseCrudService;

import java.util.Optional;
import java.util.UUID;

public interface StageService extends BaseCrudService<Stage> {
    Optional<Stage> findByNameAndFestivalId(String name, UUID festivalId);
}