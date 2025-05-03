package com.maco.followthebeat.v2.core.service.impl;

import com.maco.followthebeat.v2.core.dto.ConcertDTO;
import com.maco.followthebeat.v2.core.entity.Concert;
import com.maco.followthebeat.v2.core.generics.BaseCrudServiceImpl;
import com.maco.followthebeat.v2.core.mappers.ConcertMapper;
import com.maco.followthebeat.v2.core.repo.ConcertRepo;
import com.maco.followthebeat.v2.core.service.interfaces.ConcertService;
import com.maco.followthebeat.v2.core.specification.ConcertSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class ConcertServiceImpl extends BaseCrudServiceImpl<Concert> implements ConcertService {
    private final ConcertRepo concertRepo;
    private final ConcertMapper concertMapper;
    public ConcertServiceImpl(ConcertRepo concertRepo, ConcertMapper concertMapper) {
        super(concertRepo);
        this.concertRepo = concertRepo;
        this.concertMapper = concertMapper;
    }

    @Override
    public Page<Concert> getConcerts(Optional<String> artist, Optional<String> city, Optional<LocalDate> date, Pageable pageable) {
        Specification<Concert> spec = Specification.where(null);

        if (artist.isPresent()) {
            spec = spec.and(ConcertSpecification.hasArtistName(artist.get()));
        }

        if (city.isPresent()) {
            spec = spec.and(ConcertSpecification.hasCity(city.get()));
        }

        if (date.isPresent()) {
            spec = spec.and(ConcertSpecification.hasDate(date.get()));
        }

        return concertRepo.findAll(spec, pageable);
    }

    @Override
    public Page<ConcertDTO> convertToDTO(Page<Concert> concerts) {
        return concerts.map(concertMapper::fromEntity);
    }
}
