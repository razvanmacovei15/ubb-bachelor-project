package com.maco.followthebeat.v2.core.service.interfaces;

import com.maco.followthebeat.v2.core.dto.LineupEntryDTO;
import com.maco.followthebeat.v2.core.entity.LineupEntry;
import com.maco.followthebeat.v2.core.generics.BaseCrudService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;
import java.util.UUID;


public interface LineupEntryService extends BaseCrudService<LineupEntry> {
    List<LineupEntry> getLineupForUserId(UUID userId);
    boolean isConcertInUserLineup(UUID userId, UUID concertId);
    LineupEntry createLineupEntry(LineupEntryDTO dto);
    LineupEntry updateLineupEntry(UUID id, LineupEntryDTO dto);
    Page<LineupEntryDTO> searchLineupEntries(
            UUID userId,
            UUID concertId,
            Integer hasPriority,
            Integer hasPriorityGreaterThan,
            Integer hasCompatibilityGreaterThan,
            Integer minPriority,
            Integer minCompatibility,
            Instant addedAfter,
            Instant updatedAfter,
            Pageable pageable
    );
}
