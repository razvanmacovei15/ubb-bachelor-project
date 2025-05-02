package com.maco.followthebeat.v2.core.service.impl;

import com.maco.followthebeat.v2.core.entity.Concert;
import com.maco.followthebeat.v2.core.generics.BaseCrudServiceImpl;
import com.maco.followthebeat.v2.core.repo.ConcertRepo;
import com.maco.followthebeat.v2.core.service.interfaces.ConcertService;
import org.springframework.stereotype.Service;

@Service
public class ConcertServiceImpl extends BaseCrudServiceImpl<Concert> implements ConcertService {
    public ConcertServiceImpl(ConcertRepo concertRepo) {
        super(concertRepo);
    }
}
