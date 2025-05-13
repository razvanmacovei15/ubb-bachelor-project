package com.maco.followthebeat.v2.core.controller;

import com.maco.followthebeat.v2.core.dto.LineupEntryDTO;
import com.maco.followthebeat.v2.core.entity.LineupEntry;
import com.maco.followthebeat.v2.core.mappers.LineupEntryMapper;
import com.maco.followthebeat.v2.core.model.LineupDetailDto;
import com.maco.followthebeat.v2.core.service.interfaces.LineupEntryService;
import com.maco.followthebeat.v2.user.context.IsConnected;
import com.maco.followthebeat.v2.user.context.UserContext;
import com.maco.followthebeat.v2.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/lineup")
@RequiredArgsConstructor
@Slf4j
public class LineupEntryController {

    private final LineupEntryService lineupEntryService;
    private final LineupEntryMapper lineupEntryMapper;
    private final UserContext userContext;

    @IsConnected
    @PostMapping
    public ResponseEntity<?> addLineupEntry(@Valid @RequestBody LineupEntryDTO dto) {
        User user = userContext.getOrThrow();
        log.info("Creating lineup entry for user: {}", user.getId());
        try {
            LineupEntry saved = lineupEntryService.createLineupEntry(dto, user);
            return ResponseEntity.status(HttpStatus.CREATED).body(lineupEntryMapper.toDTO(saved));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
    @IsConnected
    @GetMapping("/search")
    public PagedModel<EntityModel<LineupEntryDTO>> searchLineupEntries(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "addedAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(required = false) Integer hasPriority,
            @RequestParam(required = false) Integer hasPriorityGreaterThan,
            @RequestParam(required = false) Integer hasCompatibilityGreaterThan,
            @RequestParam(required = false) Integer minPriority,
            @RequestParam(required = false) Integer minCompatibility,
            PagedResourcesAssembler<LineupEntryDTO> pagedResourcesAssembler
    ) {
        User user = userContext.getOrThrow();
        Pageable pageable = PageRequest.of(page, size,
                direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending());
        Page<LineupEntryDTO> result = lineupEntryService.searchLineupEntries(
                user.getId(),
                hasPriority,
                hasPriorityGreaterThan,
                hasCompatibilityGreaterThan,
                minPriority,
                minCompatibility,
                pageable
        );
        log.info("result: {}", result);
        return pagedResourcesAssembler.toModel(result);
    }

    @IsConnected
    @GetMapping("/search/details")
    public PagedModel<EntityModel<LineupDetailDto>> searchLineupDetails(
            @RequestParam Optional<String> artist,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "addedAt") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(required = false) Integer hasPriority,
            @RequestParam(required = false) Integer hasPriorityGreaterThan,
            @RequestParam(required = false) Integer hasCompatibilityGreaterThan,
            @RequestParam(required = false) Integer minPriority,
            @RequestParam(required = false) Integer minCompatibility,
            PagedResourcesAssembler<LineupDetailDto> pagedResourcesAssembler
    ) {
        User user = userContext.getOrThrow();
        Pageable pageable = PageRequest.of(page, size,
                direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending());
        Page<LineupDetailDto> result = lineupEntryService.searchLineupDetails(
                user.getId(),
                artist,
                hasPriority,
                hasPriorityGreaterThan,
                hasCompatibilityGreaterThan,
                minPriority,
                minCompatibility,
                pageable
        );
        log.info("result: {}", result);
        return pagedResourcesAssembler.toModel(result);
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
    @IsConnected
    @PutMapping("/{id}")
    public ResponseEntity<?> updateLineupEntry(@PathVariable UUID id, @Valid @RequestBody LineupEntryDTO dto) {
        try {
            User user = userContext.getOrThrow();
            LineupEntry updated = lineupEntryService.updateLineupEntry(id, dto, user);
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

    @IsConnected
    @GetMapping("/details")
    public ResponseEntity<?> getLineupDetails() {
        User user = userContext.getOrThrow();
        List<LineupDetailDto> details = lineupEntryService.getLineupDetailsByUserId(user.getId());
        if (details.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No lineup details found for user with id: " + user.getId());
        }
        return ResponseEntity.ok(details);
    }
}