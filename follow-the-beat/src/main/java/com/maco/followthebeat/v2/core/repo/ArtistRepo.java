package com.maco.followthebeat.v2.core.repo;

import com.maco.followthebeat.v2.core.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface ArtistRepo extends JpaRepository<Artist, UUID> {
    Optional<Artist> findByName(String name);
}
