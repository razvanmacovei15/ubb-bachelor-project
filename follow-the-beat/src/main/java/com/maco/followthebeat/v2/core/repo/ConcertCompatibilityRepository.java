package com.maco.followthebeat.v2.core.repo;

import com.maco.followthebeat.v2.core.entity.ConcertCompatibility;
import com.maco.followthebeat.v2.core.model.ConcertResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ConcertCompatibilityRepository extends JpaRepository<ConcertCompatibility, UUID>, JpaSpecificationExecutor<ConcertCompatibility> {
    List<ConcertCompatibility> findByUserId(UUID userId);
    List<ConcertCompatibility> findByConcertId(UUID concertId);
    boolean existsByUserIdAndConcertId(UUID userId, UUID concertId);
    List<ConcertCompatibility> findAllByUserId(UUID userId);
    //v2 apis
    @Query("""
    SELECT new com.maco.followthebeat.v2.core.model.ConcertResponseDto(
        c.id,
        a.name,
        a.imgUrl,
        cc.compatibility,
        s.startTime,
        s.date,
        f.name,
        l.name
    )
    FROM Concert c
    JOIN c.artist a
    JOIN c.location l
    LEFT JOIN c.schedule s
    LEFT JOIN Stage st ON st.id = l.id
    LEFT JOIN st.festival f
    JOIN ConcertCompatibility cc ON cc.concert.id = c.id
    WHERE cc.user.id = :userId
""")
    List<ConcertResponseDto> getAllConcertsByUserIdV2(@Param("userId") UUID userId);

    @Query("""
    SELECT new com.maco.followthebeat.v2.core.model.ConcertResponseDto(
        c.id,
        a.name,
        a.imgUrl,
        cc.compatibility,
        s.startTime,
        s.date,
        f.name,
        l.name
    )
    FROM Concert c
    JOIN c.artist a
    LEFT JOIN c.schedule s
    LEFT JOIN ConcertCompatibility cc ON cc.concert.id = c.id AND cc.user.id = :userId
    LEFT JOIN c.location l
    LEFT JOIN Stage st ON st.id = l.id
    LEFT JOIN st.festival f
    WHERE f.id = :festivalId
""")
    List<ConcertResponseDto> getAllConcertsByUserIdAndFestivalId(
            @Param("userId") UUID userId,
            @Param("festivalId") UUID festivalId
    );

}