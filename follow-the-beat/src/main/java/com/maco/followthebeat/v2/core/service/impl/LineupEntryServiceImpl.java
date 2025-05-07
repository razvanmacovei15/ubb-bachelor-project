package com.maco.followthebeat.v2.core.service.impl;

import com.maco.followthebeat.v2.core.dto.LineupEntryDTO;
import com.maco.followthebeat.v2.core.entity.Concert;
import com.maco.followthebeat.v2.core.entity.LineupEntry;
import com.maco.followthebeat.v2.core.generics.BaseCrudServiceImpl;
import com.maco.followthebeat.v2.core.mappers.LineupEntryMapper;
import com.maco.followthebeat.v2.core.repo.LineupEntryRepo;
import com.maco.followthebeat.v2.core.service.interfaces.ConcertService;
import com.maco.followthebeat.v2.core.service.interfaces.LineupEntryService;
import com.maco.followthebeat.v2.core.specification.LineupEntrySpecification;
import com.maco.followthebeat.v2.user.entity.User;
import com.maco.followthebeat.v2.user.service.interfaces.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class LineupEntryServiceImpl extends BaseCrudServiceImpl<LineupEntry> implements LineupEntryService {
    private final LineupEntryRepo repo;
    private final UserService userService;
    private final ConcertService concertService;
    private final LineupEntryMapper lineupEntryMapper;
    public LineupEntryServiceImpl(LineupEntryRepo repo,UserService userService, ConcertService concertService, LineupEntryMapper lineupEntryMapper) {
        super(repo);
        this.repo = repo;
        this.userService = userService;
        this.concertService = concertService;
        this.lineupEntryMapper = lineupEntryMapper;
    }


    @Override
    public List<LineupEntry> getLineupForUserId(UUID userId) {
        return repo.findByUserId(userId);
    }

    public Page<LineupEntryDTO> searchLineupEntries(
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
    ) {
        Specification<LineupEntry> spec = Specification.where(LineupEntrySpecification.hasUserId(userId));

        if (concertId != null) {
            spec = spec.and(LineupEntrySpecification.hasConcertId(concertId));
        }
        if (hasPriority != null) {
            spec = spec.and(LineupEntrySpecification.hasPriority(hasPriority));
        }
        if (hasPriorityGreaterThan != null) {
            spec = spec.and(LineupEntrySpecification.hasPriorityGreaterThan(hasPriorityGreaterThan));
        }
        if (hasCompatibilityGreaterThan != null) {
            spec = spec.and(LineupEntrySpecification.hasCompatibilityGreaterThan(hasCompatibilityGreaterThan));
        }
        if (minPriority != null) {
            spec = spec.and(LineupEntrySpecification.hasMinPriority(minPriority));
        }
        if (minCompatibility != null) {
            spec = spec.and(LineupEntrySpecification.hasMinCompatibility(minCompatibility));
        }
        if (addedAfter != null) {
            spec = spec.and(LineupEntrySpecification.addedAfter(addedAfter));
        }
        if (updatedAfter != null) {
            spec = spec.and(LineupEntrySpecification.updatedAfter(updatedAfter));
        }


        return repo.findAll(spec, pageable).map(lineupEntryMapper::toDTO);
    }



    @Override
    public boolean isConcertInUserLineup(UUID userId, UUID concertId) {
        return repo.existsByUserIdAndConcertId(userId, concertId);
    }

    @Override
    public LineupEntry createLineupEntry(LineupEntryDTO dto) {
        User user = userService.findUserById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
        Concert concert = concertService.getById(dto.getConcertId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid concert ID"));
        if (repo.existsByUserIdAndConcertId(user.getId(), concert.getId())) {
            throw new IllegalStateException("Concert already exists in user's lineup");
        }

        LineupEntry entry = lineupEntryMapper.toEntity(dto, user, concert);
        return repo.save(entry);
    }

    @Override
    public LineupEntry updateLineupEntry(UUID id, LineupEntryDTO dto) {
        LineupEntry existing = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Lineup entry not found"));

        User user = userService.findUserById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
        Concert concert = concertService.getById(dto.getConcertId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid concert ID"));

        existing.setUser(user);
        existing.setConcert(concert);
        existing.setNotes(dto.getNotes());
        existing.setPriority(dto.getPriority());
        existing.setCompatibility(dto.getCompatibility());

        return repo.save(existing);
    }
}
