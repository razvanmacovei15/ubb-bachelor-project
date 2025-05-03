package com.maco.followthebeat.v2.core.service.interfaces;
import com.maco.followthebeat.v2.core.entity.Festival;
import com.maco.followthebeat.v2.core.generics.BaseCrudService;

import java.util.Optional;

public interface FestivalService extends BaseCrudService<Festival> {
    Optional<Festival> getFestivalByName(String name);
}