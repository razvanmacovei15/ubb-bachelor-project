package com.maco.followthebeat.v2.core.service.impl;

import com.maco.followthebeat.v2.core.dto.ConcertDTO;
import com.maco.followthebeat.v2.core.entity.Concert;
import com.maco.followthebeat.v2.core.generics.BaseCrudServiceImpl;
import com.maco.followthebeat.v2.core.mappers.ConcertMapper;
import com.maco.followthebeat.v2.core.repo.ConcertRepo;
import com.maco.followthebeat.v2.core.service.interfaces.ConcertService;
import com.maco.followthebeat.v2.core.specification.ConcertSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

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
    public Page<Concert> getConcerts(Optional<String> artist,  Optional<LocalDate> date, Pageable pageable) {
        Specification<Concert> spec = Specification.where(null);

        if (artist.isPresent()) {
            spec = spec.and(ConcertSpecification.hasArtistName(artist.get()));
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

    @Override
    public List<Concert> getConcertsByFestivalId(UUID festivalId) {
        return concertRepo.findByFestivalId(festivalId);
    }

    @Override
    public void deleteByFestivalId(UUID festivalId) {
        concertRepo.deleteByFestivalId(festivalId);
    }

    @Override
    public void deleteConcertById(UUID concertId) {
        concertRepo.deleteById(concertId);
    }

    @Override
    public Page<ConcertDTO> findConcertsByFestivalId(
            Optional<String> artist,
            Optional<LocalDate> date,
            Pageable pageable,
            UUID festivalId
    ) {
        Specification<Concert> spec = Specification.where(ConcertSpecification.hasFestivalId(festivalId));

        if (artist.isPresent()) {
            spec = spec.and(ConcertSpecification.hasArtistName(artist.get()));
        }

        if (date.isPresent()) {
            spec = spec.and(ConcertSpecification.hasDate(date.get()));
        }

        return concertRepo.findAll(spec, pageable).map(concertMapper::fromEntity);
    }
}
