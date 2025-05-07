package com.maco.followthebeat.v2.core.repo;

import com.maco.followthebeat.v2.core.entity.LineupEntry;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LineupEntryRepo extends JpaRepository<LineupEntry, UUID>, JpaSpecificationExecutor<LineupEntry> {
    List<LineupEntry> findByUserId(UUID userId);
    boolean existsByUserIdAndConcertId(UUID userId, UUID concertId);
}
