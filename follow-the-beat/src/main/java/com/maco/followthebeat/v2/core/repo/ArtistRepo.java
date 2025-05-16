package com.maco.followthebeat.v2.core.repo;

import com.maco.followthebeat.v2.core.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface ArtistRepo extends JpaRepository<Artist, UUID> {
    Optional<Artist> findByName(String name);
    @Query("""
            SELECT DISTINCT c.artist
            FROM Festival f
            JOIN f.stages s
            JOIN s.concerts c
            WHERE f.id = :festivalId
            """)
    List<Artist> findAllArtistsByFestivalId(@Param("festivalId") UUID festivalId);
}
