package com.maco.followthebeat.feature.base.service.impl;

import com.maco.followthebeat.feature.base.entity.Concert;
import com.maco.followthebeat.feature.base.generics.BaseCrudServiceImpl;
import com.maco.followthebeat.feature.base.repo.ConcertRepo;
import com.maco.followthebeat.feature.base.service.interfaces.ConcertService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
public class ConcertServiceImpl extends BaseCrudServiceImpl<Concert> implements ConcertService {
    public ConcertServiceImpl(ConcertRepo concertRepo) {
        super(concertRepo);
    }
}
