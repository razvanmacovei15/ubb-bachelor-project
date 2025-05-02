package com.maco.followthebeat.v2.core.service.interfaces;

import com.maco.followthebeat.v2.core.entity.Artist;
import com.maco.followthebeat.v2.core.generics.BaseCrudService;

import java.util.Optional;

public interface ArtistService extends BaseCrudService<Artist> {
    Optional<Artist> findByName(String name);
}