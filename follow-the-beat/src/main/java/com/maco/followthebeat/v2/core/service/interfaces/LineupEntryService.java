package com.maco.followthebeat.v2.core.service.interfaces;

import com.maco.followthebeat.v2.core.dto.LineupEntryDTO;
import com.maco.followthebeat.v2.core.entity.LineupEntry;
import com.maco.followthebeat.v2.core.generics.BaseCrudService;
import com.maco.followthebeat.v2.core.model.LineupDetailDto;
import com.maco.followthebeat.v2.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface LineupEntryService extends BaseCrudService<LineupEntry> {
    List<LineupEntry> getLineupForUserId(UUID userId);
    boolean isConcertInUserLineup(UUID userId, UUID concertId);
    LineupEntry createLineupEntry(LineupEntryDTO dto, User user);
    LineupEntry updateLineupEntry(UUID id, LineupEntryDTO dto, User user);
    Page<LineupEntryDTO> searchLineupEntries(
            UUID userId,
            Integer hasCompatibilityGreaterThan,
            Integer minCompatibility,
            Pageable pageable
    );
    List<LineupDetailDto> getLineupDetailsByUserId(UUID userId);
    Page<LineupDetailDto> searchLineupDetails(
            UUID userId,
            Optional<String> artistName,
            Integer hasCompatibilityGreaterThan,
            Integer minCompatibility,
            Pageable pageable
    );
    List<UUID> getAllConcertIdsByUserId(UUID userId);
}
