package com.maco.followthebeat.v2.core.controller;

import com.maco.followthebeat.v2.core.dto.ConcertDTO;
import com.maco.followthebeat.v2.core.entity.Concert;
import com.maco.followthebeat.v2.core.service.interfaces.ConcertService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/concerts")
@RequiredArgsConstructor
public class ConcertController {

    private final ConcertService concertService;

    @GetMapping
    public PagedModel<EntityModel<ConcertDTO>> getConcerts(
            @RequestParam Optional<String> artist,
            @RequestParam Optional<String> city,
            @RequestParam Optional<LocalDate> date,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            PagedResourcesAssembler<ConcertDTO> pagedResourcesAssembler
    ) {
        Pageable pageable = PageRequest.of(page, size,
                direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending());

        //todo make in only one method
        Page<Concert> concerts = concertService.getConcerts(artist, city, date, pageable);
        Page<ConcertDTO> dtoPage = concertService.convertToDTO(concerts);

        //todo de intrebat pe alex care e treaba cu pagedResourcesAssembler
        return pagedResourcesAssembler.toModel(dtoPage);
    }
}