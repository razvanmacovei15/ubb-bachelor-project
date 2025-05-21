package com.maco.followthebeat.v2.core.service.impl;

import com.maco.client.v2.SpotifyClient;
import com.maco.followthebeat.v2.core.dto.ConcertCompatibilityDto;
import com.maco.followthebeat.v2.core.dto.LineupEntryDTO;
import com.maco.followthebeat.v2.core.entity.*;
import com.maco.followthebeat.v2.core.generics.BaseCrudServiceImpl;
import com.maco.followthebeat.v2.core.mappers.LineupEntryMapper;
import com.maco.followthebeat.v2.core.model.LineupDetailDto;
import com.maco.followthebeat.v2.core.repo.LineupEntryRepo;
import com.maco.followthebeat.v2.core.service.interfaces.ArtistService;
import com.maco.followthebeat.v2.core.service.interfaces.ConcertCompatibilityService;
import com.maco.followthebeat.v2.core.service.interfaces.ConcertService;
import com.maco.followthebeat.v2.core.service.interfaces.LineupEntryService;
import com.maco.followthebeat.v2.core.specification.ConcertSpecification;
import com.maco.followthebeat.v2.core.specification.LineupEntrySpecification;
import com.maco.followthebeat.v2.spotify.auth.client.SpotifyClientManager;
import com.maco.followthebeat.v2.user.entity.User;
import com.maco.followthebeat.v2.user.service.interfaces.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class LineupEntryServiceImpl extends BaseCrudServiceImpl<LineupEntry> implements LineupEntryService {
    private final LineupEntryRepo repo;
    private final ConcertService concertService;
    private final LineupEntryMapper lineupEntryMapper;
    private final LineupEntryRepo lineupEntryRepo;
    private final SpotifyClientManager spotifyClientManager;
    private final ArtistService artistService;
    private final ConcertCompatibilityService concertCompatibilityService;

    public LineupEntryServiceImpl(LineupEntryRepo repo,
                                  ConcertService concertService,
                                  LineupEntryMapper lineupEntryMapper,
                                  LineupEntryRepo lineupEntryRepo,
                                  SpotifyClientManager spotifyClientManager,
                                  ArtistService artistService,ConcertCompatibilityService concertCompatibilityService) {
        super(repo);
        this.repo = repo;
        this.concertService = concertService;
        this.lineupEntryMapper = lineupEntryMapper;
        this.lineupEntryRepo = lineupEntryRepo;
        this.spotifyClientManager = spotifyClientManager;
        this.artistService = artistService;
        this.concertCompatibilityService = concertCompatibilityService;
    }

    @Override
    public List<LineupEntry> getLineupForUserId(UUID userId) {
        return repo.findByUserId(userId);
    }

    @Override
    public LineupEntry getLineupByConcertIdAndUserId(UUID concertId, UUID userId) {
        return repo.findByConcertIdAndUserId(concertId, userId)
                .orElseThrow(() -> new NoSuchElementException("Lineup entry not found"));
    }

    public Page<LineupEntryDTO> searchLineupEntries(
            UUID userId,
            Integer hasCompatibilityGreaterThan,
            Integer minCompatibility,
            Pageable pageable
    ) {
        Specification<LineupEntry> spec = Specification.where(LineupEntrySpecification.hasUserId(userId));

        return repo.findAll(spec, pageable).map(lineupEntryMapper::toDTO);
    }

    @Override
    public List<LineupDetailDto> getLineupDetailsByUserId(UUID userId) {
        return lineupEntryRepo.findLineupDetailsByUserId(userId);
    }

    @Override
    public Page<LineupDetailDto> searchLineupDetails(
            UUID userId,
            Optional<String> artistName,
            Pageable pageable
    ) {
        Specification<LineupEntry> spec = Specification.where(LineupEntrySpecification.hasUserId(userId));
        if (artistName.isPresent()) {
            spec = spec.and(LineupEntrySpecification.hasArtistName(artistName.get()));
        }

        return lineupEntryRepo.findAll(spec, pageable)
                .map(entry -> {
                    Concert concert = entry.getConcertCompatibility().getConcert();
                    Artist artist = concert.getArtist();
                    Schedule schedule = concert.getSchedule();
                    Location location = concert.getLocation();
                    Stage stage = (location instanceof Stage) ? (Stage) location : null;

                    return new LineupDetailDto(
                            entry.getId(),
                            concert.getId(),
                            artist.getName(),
                            artist.getImgUrl(),
                            concert.getArtist().getSpotifyUrl(),
                            entry.getNotes(),
                            entry.getPriority(),
                            entry.getConcertCompatibility().getCompatibility(),
                            schedule != null ? schedule.getStartTime() : null,
                            schedule != null ? schedule.getDate() : null,
                            location != null ? location.getName() : null,
                            stage != null && stage.getFestival() != null ? stage.getFestival().getName() : null
                    );
                });
    }

    @Override
    public List<UUID> getAllConcertIdsByUserId(UUID userId) {
        return repo.findAllConcertIds(userId);
    }


    @Override
    public boolean isConcertInUserLineup(UUID userId, UUID concertId) {
        return repo.existsByConcertCompatibility_User_IdAndConcertCompatibility_Concert_Id(userId, concertId);
    }

    @Override
    public LineupEntry createLineupEntry(LineupEntryDTO dto, User user) {
        log.info("Creating lineup entry for user: {}", user.getId());
        if (dto.getConcertId() == null) {
            throw new IllegalArgumentException("Concert ID cannot be null");
        }
        Concert concert = concertService.getById(dto.getConcertId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid concert ID"));
        log.info("Concert found: {}", concert.getId());

        if (repo.existsByConcertCompatibility_User_IdAndConcertCompatibility_Concert_Id(user.getId(), concert.getId())) {
            throw new IllegalStateException("Concert already exists in user's lineup");
        }
        ConcertCompatibility concertCompatibility = concertCompatibilityService.getByConcertIdAndUserId(concert.getId(), user.getId());
        if(concert.getArtist().getSpotifyUrl() != null) {
            log.info("Spotify URL already set: {}", concert.getArtist().getSpotifyUrl());

            return repo.save(lineupEntryMapper.toEntity(dto, concertCompatibility));
        }

        String artistName = concert.getArtist().getName();
        log.info("Artist name that we need to search: {}", artistName);
        SpotifyClient spotifyClient = spotifyClientManager.getOrCreateSpotifyClient(user.getId());
        log.info(spotifyClient.searchForArtist(artistName).get(0).getName());
        String spotifyUrl = spotifyClient.searchForArtist(artistName).get(0).getUri();
        Artist updatedArtist = concert.getArtist();
        updatedArtist.setSpotifyUrl(spotifyUrl);
        artistService.update(updatedArtist.getId(), updatedArtist);
        log.info("Spotify URL set: {}", spotifyUrl);
        LineupEntry entry = lineupEntryMapper.toEntity(dto, concertCompatibility);
        log.info("Lineup entry created: {}", entry.getConcertCompatibility().getConcert().getArtist().getSpotifyUrl());
        return repo.save(entry);
    }

    @Override
    public LineupEntry updateLineupEntry(LineupEntryDTO dto, User user) {
        LineupEntry existing = repo.findByConcertIdAndUserId(dto.getConcertId(), user.getId())
                .orElseThrow(() -> new NoSuchElementException("Lineup entry not found"));

        existing.setNotes(dto.getNotes());
        existing.setPriority(dto.getPriority());

        return repo.save(existing);
    }
}
