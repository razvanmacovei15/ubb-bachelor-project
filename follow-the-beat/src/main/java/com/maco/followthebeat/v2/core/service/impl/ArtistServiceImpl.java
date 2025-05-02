package com.maco.followthebeat.v2.core.service.impl;

import com.maco.followthebeat.v2.core.entity.Artist;
import com.maco.followthebeat.v2.core.generics.BaseCrudServiceImpl;
import com.maco.followthebeat.v2.core.repo.ArtistRepo;
import com.maco.followthebeat.v2.core.service.interfaces.ArtistService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ArtistServiceImpl extends BaseCrudServiceImpl<Artist> implements ArtistService {
    private final ArtistRepo artistRepo;
    public ArtistServiceImpl(ArtistRepo artistRepo) {
        super(artistRepo);
        this.artistRepo = artistRepo;
    }


    @Override
    public Optional<Artist> findByName(String name) {
        return artistRepo.findByName(name);
    }
}
