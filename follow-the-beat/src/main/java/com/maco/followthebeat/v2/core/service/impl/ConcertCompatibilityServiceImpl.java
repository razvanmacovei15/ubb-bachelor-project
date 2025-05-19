package com.maco.followthebeat.v2.core.service.impl;

import com.maco.followthebeat.v2.core.dto.ConcertCompatibilityDto;
import com.maco.followthebeat.v2.core.entity.*;
import com.maco.followthebeat.v2.core.model.ConcertResponseDto;
import com.maco.followthebeat.v2.core.repo.ConcertCompatibilityRepository;
import com.maco.followthebeat.v2.core.service.interfaces.ConcertCompatibilityService;
import com.maco.followthebeat.v2.core.service.interfaces.ConcertService;
import com.maco.followthebeat.v2.core.specification.ConcertCompatibilitySpecification;
import com.maco.followthebeat.v2.user.service.interfaces.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConcertCompatibilityServiceImpl implements ConcertCompatibilityService {

    private final ConcertCompatibilityRepository repository;
    private final UserService userService;
    private final ConcertService concertService;

    @Override
    public ConcertCompatibilityDto create(ConcertCompatibilityDto dto) {
        ConcertCompatibility entity = ConcertCompatibility.builder()
                .user(userService.findUserById(dto.userId()).orElseThrow())
                .concert(concertService.getById(dto.concertId()).orElseThrow())
                .compatibility(dto.compatibility())
                .build();
        return toDto(repository.save(entity));
    }

    @Override
    public List<ConcertCompatibilityDto> getAll() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public ConcertCompatibilityDto getById(UUID id) {
        return toDto(repository.findById(id).orElseThrow());
    }

    @Override
    public List<ConcertCompatibilityDto> getByUser(UUID userId) {
        return repository.findByUserId(userId).stream().map(this::toDto).toList();
    }

    @Override
    public List<ConcertCompatibilityDto> getByConcert(UUID concertId) {
        return repository.findByConcertId(concertId).stream().map(this::toDto).toList();
    }

    @Override
    public ConcertCompatibilityDto update(UUID id, ConcertCompatibilityDto dto) {
        ConcertCompatibility entity = repository.findById(id).orElseThrow();
        entity.setCompatibility(dto.compatibility());
        return toDto(repository.save(entity));
    }

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public Page<ConcertResponseDto> searchConcertsWithCompatibility(UUID userId, Optional<String> artistName, Float minCompatibility, Float hasCompatibilityGreaterThan, Pageable pageable) {
        Specification<ConcertCompatibility> spec = Specification.where(ConcertCompatibilitySpecification.hasUserId(userId));
        if(artistName.isPresent()){
            spec = spec.and(ConcertCompatibilitySpecification.hasArtistName(artistName.get()));
        }
        if(hasCompatibilityGreaterThan != null){
            spec = spec.and(ConcertCompatibilitySpecification.hasCompatibilityGreaterThan(hasCompatibilityGreaterThan));
        }
        if(minCompatibility != null){
            spec = spec.and(ConcertCompatibilitySpecification.hasMinCompatibility(minCompatibility));
        }
        return repository.findAll(spec, pageable)
                .map(concertCompatibility -> {
                    Concert concert = concertCompatibility.getConcert();
                    Artist artist = concert.getArtist();
                    Schedule schedule = concert.getSchedule();
                    Location location = concert.getLocation();
                    Stage stage = (location instanceof Stage) ? (Stage) location : null;

                    return new ConcertResponseDto(
                            concert.getId(),
                            artist.getName(),
                            artist.getImgUrl(),
                            concertCompatibility.getCompatibility(),
                            schedule != null ? schedule.getStartTime() : null,
                            schedule != null ? schedule.getDate() : null,
                            stage != null && stage.getFestival() != null ? stage.getFestival().getName() : null,
                            location != null ? location.getName() : null
                    );
                });
    }

    @Override
    public Page<ConcertResponseDto> searchConcertsWithCompatibilityByFestivalId(UUID userId, UUID festivalId, Optional<String> artistName, Float minCompatibility, Float hasCompatibilityGreaterThan, Pageable pageable) {
        Specification<ConcertCompatibility> spec = Specification.where(ConcertCompatibilitySpecification.hasUserId(userId));
        if(festivalId != null){
            spec = spec.and(ConcertCompatibilitySpecification.hasFestivalId(festivalId));
        }
        if(artistName.isPresent()){
            spec = spec.and(ConcertCompatibilitySpecification.hasArtistName(artistName.get()));
        }
        if(hasCompatibilityGreaterThan != null){
            spec = spec.and(ConcertCompatibilitySpecification.hasCompatibilityGreaterThan(hasCompatibilityGreaterThan));
        }
        if(minCompatibility != null){
            spec = spec.and(ConcertCompatibilitySpecification.hasMinCompatibility(minCompatibility));
        }
        return repository.findAll(spec, pageable)
                .map(concertCompatibility -> {
                    Concert concert = concertCompatibility.getConcert();
                    Artist artist = concert.getArtist();
                    Schedule schedule = concert.getSchedule();
                    Location location = concert.getLocation();
                    Stage stage = (location instanceof Stage) ? (Stage) location : null;

                    return new ConcertResponseDto(
                            concert.getId(),
                            artist.getName(),
                            artist.getImgUrl(),
                            concertCompatibility.getCompatibility(),
                            schedule != null ? schedule.getStartTime() : null,
                            schedule != null ? schedule.getDate() : null,
                            stage != null && stage.getFestival() != null ? stage.getFestival().getName() : null,
                            location != null ? location.getName() : null
                    );
                });
    }

    @Transactional
    @Override
    public void initCompatibilities(UUID userId) {
        List<Concert> concerts = concertService.getAll();
        List<UUID> existingConcertIds = repository
                .findAllByUserId(userId)
                .stream()
                .map(cc -> cc.getConcert().getId())
                .toList();

        for (Concert c : concerts) {
            if (!existingConcertIds.contains(c.getId())) {
                ConcertCompatibilityDto dto = ConcertCompatibilityDto.builder()
                        .compatibility(0.0f)
                        .userId(userId)
                        .concertId(c.getId())
                        .build();
                create(dto);
            }
        }
    }

    private ConcertCompatibilityDto toDto(ConcertCompatibility entity) {
        return new ConcertCompatibilityDto(
                entity.getId(),
                entity.getUser().getId(),
                entity.getConcert().getId(),
                entity.getCompatibility()
        );
    }
}
