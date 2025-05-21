package com.maco.followthebeat.v2.core.service.impl;

import com.maco.followthebeat.v2.core.dto.ConcertDTO;
import com.maco.followthebeat.v2.core.dto.ConcertForSuggestionDto;
import com.maco.followthebeat.v2.core.entity.Artist;
import com.maco.followthebeat.v2.core.entity.Concert;
import com.maco.followthebeat.v2.core.generics.BaseCrudServiceImpl;
import com.maco.followthebeat.v2.core.mappers.ConcertMapper;
import com.maco.followthebeat.v2.core.model.ConcertResponseDto;
import com.maco.followthebeat.v2.core.repo.ConcertRepo;
import com.maco.followthebeat.v2.core.service.interfaces.ArtistService;
import com.maco.followthebeat.v2.core.service.interfaces.ConcertService;
import com.maco.followthebeat.v2.core.specification.ConcertSpecification;
import com.maco.followthebeat.v2.user.context.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Stream;
@Slf4j
@Service
public class ConcertServiceImpl extends BaseCrudServiceImpl<Concert> implements ConcertService {
    private final ConcertRepo concertRepo;
    private final ConcertMapper concertMapper;
    private final ArtistService artistService;
    private final UserContext userContext;
    private final Executor taskExecutor;
    public ConcertServiceImpl(ConcertRepo concertRepo,
                              ConcertMapper concertMapper,
                              ArtistService artistService,
                              UserContext userContext,
                              @Qualifier("spotifyTaskExecutor") Executor taskExecutor) {
        super(concertRepo);
        this.concertRepo = concertRepo;
        this.concertMapper = concertMapper;
        this.artistService = artistService;
        this.userContext = userContext;
        this.taskExecutor = taskExecutor;
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
    public Page<ConcertResponseDto> convertToDTO(Page<Concert> concerts) {
        return concerts.map(concertMapper::fromEntityToResponseDto);
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
    public Page<ConcertResponseDto> findConcertsByFestivalId(
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

        return concertRepo.findAll(spec, pageable).map(concertMapper::fromEntityToResponseDto);
    }

    @Override
    public long countConcertsByArtistName(String artistName) {
        return concertRepo.countConcertsByArtistName(artistName);
    }

    private CompletableFuture<ConcertForSuggestionDto> updateAndMapAsync(Concert concert, UUID userId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                log.info("Fetching details for artist: {}", concert.getArtist().getName());
                Artist updatedArtist = artistService.fetchDetailsFromSpotify(concert.getArtist(), userId);
                log.info("Fetched details for artist: {}", updatedArtist.getName());
                return concertMapper.toSuggestionDto(concert);
            } catch (Exception e) {
                // Log and fall back to mapping existing artist
                return concertMapper.toSuggestionDto(concert);
            }
        }, taskExecutor);
    }

    @Override
    public List<ConcertForSuggestionDto> generateFestivalPayload(UUID festivalId) {
        List<Concert> concerts = getConcertsByFestivalId(festivalId);
        log.info("Generating festival payload for festival ID: {}", festivalId);
        List<Artist> artists = concerts.stream()
                .map(Concert::getArtist).distinct()
                .toList();

        log.info("Found {} artists for festival ID: {}", artists.size(), festivalId);
        UUID userId = userContext.getOrThrow().getId();
        log.info("User ID: {}", userId);
        List<CompletableFuture<ConcertForSuggestionDto>> futures = concerts.stream()
                .map(concert -> updateAndMapAsync(concert, userId))
                .toList();
        log.info("Generated {} futures for artists", futures.size());
        log.info("Waiting for all futures to complete");
        return futures.stream()
                .map(CompletableFuture::join)
                .toList();
    }
}
