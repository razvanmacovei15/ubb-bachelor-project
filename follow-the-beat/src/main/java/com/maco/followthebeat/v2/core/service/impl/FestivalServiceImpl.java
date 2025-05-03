package com.maco.followthebeat.v2.core.service.impl;

import com.maco.followthebeat.v2.core.entity.Festival;
import com.maco.followthebeat.v2.core.generics.BaseCrudServiceImpl;
import com.maco.followthebeat.v2.core.repo.FestivalRepo;
import com.maco.followthebeat.v2.core.service.interfaces.FestivalService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FestivalServiceImpl extends BaseCrudServiceImpl<Festival> implements FestivalService {
    private final FestivalRepo festivalRepo;
    public FestivalServiceImpl(FestivalRepo festivalRepo) {
        super(festivalRepo);
        this.festivalRepo = festivalRepo;
    }

    @Override
    public Optional<Festival> getFestivalByName(String name) {
        return festivalRepo.findByName(name);
    }
}
