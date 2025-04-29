package com.maco.followthebeat.feature.base.service.impl;

import com.maco.followthebeat.feature.base.entity.Artist;
import com.maco.followthebeat.feature.base.generics.BaseCrudServiceImpl;
import com.maco.followthebeat.feature.base.repo.ArtistRepo;
import com.maco.followthebeat.feature.base.service.interfaces.ArtistService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
public class ArtistServiceImpl extends BaseCrudServiceImpl<Artist> implements ArtistService {
    public ArtistServiceImpl(ArtistRepo artistRepo) {
        super(artistRepo);
    }
}
