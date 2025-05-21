package com.maco.followthebeat.v2.core.controller;

import com.maco.followthebeat.v2.core.dto.ConcertDTO;
import com.maco.followthebeat.v2.core.entity.Concert;
import com.maco.followthebeat.v2.core.model.ConcertResponseDto;
import com.maco.followthebeat.v2.core.model.LineupDetailDto;
import com.maco.followthebeat.v2.core.service.interfaces.ConcertCompatibilityService;
import com.maco.followthebeat.v2.core.service.interfaces.ConcertService;
import com.maco.followthebeat.v2.user.context.IsConnected;
import com.maco.followthebeat.v2.user.context.UserContext;
import com.maco.followthebeat.v2.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v2/concerts")
@RequiredArgsConstructor
public class ConcertControllerV2 {

    private final ConcertCompatibilityService concertCompatibilityService;
    private final UserContext userContext;

    @IsConnected
    @GetMapping
    public PagedModel<EntityModel<ConcertResponseDto>> getAllConcerts(
            @RequestParam Optional<String> artist,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "addedAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(required = false) Float hasCompatibilityGreaterThan,
            @RequestParam(required = false) Float minCompatibility,
            PagedResourcesAssembler<ConcertResponseDto> pagedResourcesAssembler
    ) {
        User user = userContext.getOrThrow();
        log.info("[getAllConcerts] userId={}, artist={}, minCompatibility={}, hasCompatibilityGreaterThan={}, page={}, size={}, sortBy={}, direction={}",
                user.getId(), artist.orElse(""), minCompatibility, hasCompatibilityGreaterThan, page, size, sortBy, direction);

        Pageable pageable = PageRequest.of(page, size,
                direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending());

        Page<ConcertResponseDto> concerts = concertCompatibilityService.searchConcertsWithCompatibility(
                user.getId(),
                artist,
                minCompatibility,
                hasCompatibilityGreaterThan,
                pageable
        );

        return pagedResourcesAssembler.toModel(concerts);
    }

    @IsConnected
    @GetMapping("/festival")
    public PagedModel<EntityModel<ConcertResponseDto>> getAllConcertsByFestivalId(
            @RequestParam UUID festivalId,
            @RequestParam Optional<String> artist,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "addedAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(required = false) Float hasCompatibilityGreaterThan,
            @RequestParam(required = false) Float minCompatibility,
            PagedResourcesAssembler<ConcertResponseDto> pagedResourcesAssembler
    ) {
        User user = userContext.getOrThrow();
        log.info("[getAllConcertsByFestivalId] userId={}, festivalId={}, artist={}, minCompatibility={}, hasCompatibilityGreaterThan={}, page={}, size={}, sortBy={}, direction={}",
                user.getId(), festivalId, artist.orElse(""), minCompatibility, hasCompatibilityGreaterThan, page, size, sortBy, direction);

        Pageable pageable = PageRequest.of(page, size,
                direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending());

        Page<ConcertResponseDto> concerts = concertCompatibilityService.searchConcertsWithCompatibilityByFestivalId(
                user.getId(),
                festivalId,
                artist,
                minCompatibility,
                hasCompatibilityGreaterThan,
                pageable
        );

        log.info("[getAllConcertsByFestivalId] resultSize={}", concerts.getTotalElements());
        return pagedResourcesAssembler.toModel(concerts);
    }
}
