package com.maco.followthebeat.v2.core.repo;

import com.maco.followthebeat.v2.core.dto.ConcertDTO;
import com.maco.followthebeat.v2.core.entity.Concert;
import com.maco.followthebeat.v2.core.model.ConcertResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
@Repository
public interface ConcertRepo extends JpaRepository<Concert, UUID>, JpaSpecificationExecutor<Concert> {
    @Query("SELECT c FROM Concert c WHERE TREAT(c.location AS Stage).festival.id = :festivalId")
    List<Concert> findByFestivalId(@Param("festivalId") UUID festivalId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Concert c WHERE TREAT(c.location AS Stage).festival.id = :festivalId")
    void deleteByFestivalId(@Param("festivalId") UUID festivalId);

    @Query("""
    SELECT c
    FROM Concert c
    JOIN Stage s ON c.location.id = s.id
    JOIN Festival f ON s.festival.id = f.id
    WHERE f.id = :festivalId
""")
    List<Concert> findConcertsByFestivalId(@Param("festivalId") UUID festivalId);

    @Query("SELECT COUNT(c) FROM Concert c WHERE LOWER(c.artist.name) = LOWER(:artistName)")
    long countConcertsByArtistName(@Param("artistName") String artistName);

}
