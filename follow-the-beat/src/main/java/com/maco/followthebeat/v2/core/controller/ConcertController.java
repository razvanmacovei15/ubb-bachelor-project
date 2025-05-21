package com.maco.followthebeat.v2.core.controller;

import com.maco.followthebeat.v2.core.dto.ConcertDTO;
import com.maco.followthebeat.v2.core.entity.Concert;
import com.maco.followthebeat.v2.core.model.ConcertResponseDto;
import com.maco.followthebeat.v2.core.service.interfaces.ConcertService;
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
@RequestMapping("/api/v1/concerts")
@RequiredArgsConstructor
public class ConcertController {

    private final ConcertService concertService;

    @GetMapping
    public PagedModel<EntityModel<ConcertResponseDto>> getConcerts(
            @RequestParam Optional<String> artist,
            @RequestParam Optional<LocalDate> date,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            PagedResourcesAssembler<ConcertResponseDto> pagedResourcesAssembler
    ) {
        Pageable pageable = PageRequest.of(page, size,
                direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending());

        Page<Concert> concerts = concertService.getConcerts(artist, date, pageable);
        log.info("first concert artist: {}", concerts.getContent().getFirst().getArtist().getName());
        log.info("first concert spotify url: {}", concerts.getContent().getFirst().getArtist().getSpotifyUrl());
        Page<ConcertResponseDto> dtoPage = concertService.convertToDTO(concerts);
        dtoPage.getContent().forEach(concert -> {
            log.info("Concert ID: {}, Artist: {}", concert.getConcertId(), concert.getArtistName());
        });
        return pagedResourcesAssembler.toModel(dtoPage);
    }

    @GetMapping("/by-festival")
    public PagedModel<EntityModel<ConcertResponseDto>> getConcertsByFestival(
            @RequestParam UUID festivalId,
            @RequestParam Optional<String> artist,
            @RequestParam Optional<LocalDate> date,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            PagedResourcesAssembler<ConcertResponseDto> pagedResourcesAssembler
    ) {
        Pageable pageable = PageRequest.of(page, size,
                direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending());

        Page<ConcertResponseDto> concertPage = concertService.findConcertsByFestivalId(artist, date, pageable, festivalId);

        concertPage.getContent().forEach(concert -> {
            log.info("Concert ID: {}, Artist: {}", concert.getConcertId(), concert.getArtistName());
        });
        return pagedResourcesAssembler.toModel(concertPage);
    }

    @GetMapping("/count-by-artist")
    public ResponseEntity<Long> countConcertsByArtist(@RequestParam String artistName) {
        long count = concertService.countConcertsByArtistName(artistName);
        return ResponseEntity.ok(count);
    }
}