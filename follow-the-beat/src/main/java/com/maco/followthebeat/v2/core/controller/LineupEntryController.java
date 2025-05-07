package com.maco.followthebeat.v2.core.controller;

import com.maco.followthebeat.v2.core.dto.LineupEntryDTO;
import com.maco.followthebeat.v2.core.entity.LineupEntry;
import com.maco.followthebeat.v2.core.mappers.LineupEntryMapper;
import com.maco.followthebeat.v2.core.service.interfaces.LineupEntryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/lineup")
@RequiredArgsConstructor
public class LineupEntryController {

    private final LineupEntryService lineupEntryService;
    private final LineupEntryMapper lineupEntryMapper;

    @PostMapping
    public ResponseEntity<?> addLineupEntry(@Valid @RequestBody LineupEntryDTO dto) {
        try {
            LineupEntry saved = lineupEntryService.createLineupEntry(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(lineupEntryMapper.toDTO(saved));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Page<LineupEntryDTO>> searchLineupEntries(
            @RequestParam(required = false) UUID userId,
            @RequestParam(required = false) UUID concertId,
            @RequestParam(required = false) Integer hasPriority,
            @RequestParam(required = false) Integer hasPriorityGreaterThan,
            @RequestParam(required = false) Integer hasCompatibilityGreaterThan,
            @RequestParam(required = false) Integer minPriority,
            @RequestParam(required = false) Integer minCompatibility,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant addedAfter,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant updatedAfter,
            Pageable pageable
    ) {
        Page<LineupEntryDTO> result = lineupEntryService.searchLineupEntries(
                userId,
                concertId,
                hasPriority,
                hasPriorityGreaterThan,
                hasCompatibilityGreaterThan,
                minPriority,
                minCompatibility,
                addedAfter,
                updatedAfter,
                pageable
        );
        return ResponseEntity.ok(result);
    }




    @GetMapping("/{id}")
    public ResponseEntity<?> getLineupEntry(@PathVariable UUID id) {
        Optional<LineupEntry> entry = lineupEntryService.getById(id);
        if (entry.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Lineup entry not found with id: " + id);
        }

        LineupEntryDTO dto = lineupEntryMapper.toDTO(entry.get());
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserLineup(@PathVariable UUID userId) {
        List<LineupEntry> lineup = lineupEntryService.getLineupForUserId(userId);
        if (lineup.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No lineup entries found for user with id: " + userId);
        }
        List<LineupEntryDTO> dtoList = lineup.stream()
                .map(lineupEntryMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateLineupEntry(@PathVariable UUID id, @Valid @RequestBody LineupEntryDTO dto) {
        try {
            LineupEntry updated = lineupEntryService.updateLineupEntry(id, dto);
            return ResponseEntity.ok(lineupEntryMapper.toDTO(updated));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeLineupEntry(@PathVariable UUID id) {
        Optional<LineupEntry> existing = lineupEntryService.getById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Lineup entry not found with id: " + id);
        }
        lineupEntryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}