package com.maco.followthebeat.feature.base.service.impl;

import com.maco.followthebeat.feature.base.entity.Festival;
import com.maco.followthebeat.feature.base.generics.BaseCrudService;
import com.maco.followthebeat.feature.base.generics.BaseCrudServiceImpl;
import com.maco.followthebeat.feature.base.repo.FestivalRepo;
import com.maco.followthebeat.feature.base.service.interfaces.FestivalService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
public class FestivalServiceImpl extends BaseCrudServiceImpl<Festival> implements FestivalService {
    public FestivalServiceImpl(FestivalRepo festivalRepo) {
        super(festivalRepo);
    }
}
