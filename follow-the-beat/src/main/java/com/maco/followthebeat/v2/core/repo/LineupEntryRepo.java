package com.maco.followthebeat.v2.core.repo;

import com.maco.followthebeat.v2.core.entity.ConcertCompatibility;
import com.maco.followthebeat.v2.core.entity.LineupEntry;
import com.maco.followthebeat.v2.core.model.LineupDetailDto;
import com.maco.followthebeat.v2.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LineupEntryRepo extends JpaRepository<LineupEntry, UUID>, JpaSpecificationExecutor<LineupEntry> {
    @Query("SELECT le FROM LineupEntry le WHERE le.concertCompatibility.user.id = :userId")
    List<LineupEntry> findByUserId(UUID userId);
    @Query("SELECT le FROM LineupEntry le WHERE le.concertCompatibility.concert.id = :concertId AND le.concertCompatibility.user.id = :userId")
    Optional<LineupEntry> findByConcertIdAndUserId(UUID concertId, UUID userId);
    @Query("SELECT le FROM LineupEntry le WHERE le.concertCompatibility.user.id = :userId AND le.concertCompatibility.concert.id = :concertId")
    boolean existsByUserIdAndConcertId(UUID userId, UUID concertId);
    boolean existsByConcertCompatibility_User_IdAndConcertCompatibility_Concert_Id(UUID userId, UUID concertId);

    @Query("""
    SELECT new com.maco.followthebeat.v2.core.model.LineupDetailDto(
        le.id,
        le.concertCompatibility.concert.id,
        a.name,
        a.imgUrl,
        a.spotifyUrl,
        le.notes,
        le.priority,
        le.concertCompatibility.compatibility,
        s.startTime,
        s.date,
        l.name,
        f.name
    )
    FROM LineupEntry le
    JOIN le.concertCompatibility.concert c
    JOIN c.artist a
    LEFT JOIN c.schedule s
    LEFT JOIN c.location l
    LEFT JOIN Stage st ON st.id = l.id
    LEFT JOIN st.festival f
    WHERE le.concertCompatibility.user.id = :userId
""")
    List<LineupDetailDto> findLineupDetailsByUserId(@Param("userId") UUID userId);
    @Query("SELECT le.concertCompatibility.concert.id FROM LineupEntry le WHERE le.concertCompatibility.user.id = :userId")
    List<UUID> findAllConcertIds(@Param("userId") UUID userId);
}
