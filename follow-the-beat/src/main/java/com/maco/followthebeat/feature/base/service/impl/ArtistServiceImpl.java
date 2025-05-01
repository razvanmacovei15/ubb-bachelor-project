package com.maco.followthebeat.feature.base.service.impl;

import com.maco.followthebeat.feature.base.entity.Artist;
import com.maco.followthebeat.feature.base.generics.BaseCrudServiceImpl;
import com.maco.followthebeat.feature.base.repo.ArtistRepo;
import com.maco.followthebeat.feature.base.service.interfaces.ArtistService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
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
