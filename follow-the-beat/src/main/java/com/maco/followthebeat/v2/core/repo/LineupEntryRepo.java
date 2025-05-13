package com.maco.followthebeat.v2.core.repo;

import com.maco.followthebeat.v2.core.entity.LineupEntry;
import com.maco.followthebeat.v2.core.model.LineupDetailDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LineupEntryRepo extends JpaRepository<LineupEntry, UUID>, JpaSpecificationExecutor<LineupEntry> {
    List<LineupEntry> findByUserId(UUID userId);
    boolean existsByUserIdAndConcertId(UUID userId, UUID concertId);
    @Query("""
    SELECT new com.maco.followthebeat.v2.core.model.LineupDetailDto(
        le.id,
        a.name,
        a.imgUrl,
        null,
        le.notes,
        le.priority,
        le.compatibility,
        s.startTime,
        s.date,
        l.name,
        f.name
    )
    FROM LineupEntry le
    JOIN le.concert c
    JOIN c.artist a
    LEFT JOIN c.schedule s
    LEFT JOIN c.location l
    LEFT JOIN Stage st ON st.id = l.id
    LEFT JOIN st.festival f
    WHERE le.user.id = :userId
""")
    List<LineupDetailDto> findLineupDetailsByUserId(@Param("userId") UUID userId);
}
