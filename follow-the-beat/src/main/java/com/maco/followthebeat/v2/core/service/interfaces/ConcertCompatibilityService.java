package com.maco.followthebeat.v2.core.service.interfaces;

import com.maco.followthebeat.v2.core.dto.ConcertCompatibilityDto;
import com.maco.followthebeat.v2.core.entity.ConcertCompatibility;
import com.maco.followthebeat.v2.core.model.ConcertResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConcertCompatibilityService {
    ConcertCompatibilityDto create(ConcertCompatibilityDto dto);
    ConcertCompatibility getByConcertIdAndUserId(UUID concertId, UUID userId);
    List<ConcertCompatibilityDto> getAll();
    ConcertCompatibilityDto getByConcertAndUser(UUID concertId, UUID userId);
    ConcertCompatibilityDto getById(UUID id);
    List<ConcertCompatibilityDto> getByUser(UUID userId);
    List<ConcertCompatibilityDto> getByConcert(UUID concertId);
    ConcertCompatibilityDto update(UUID id, ConcertCompatibilityDto dto);
    void delete(UUID id);
    Page<ConcertResponseDto> searchConcertsWithCompatibility(
            UUID userId,
            Optional<String> artistName,
            Float minCompatibility,
            Float hasCompatibilityGreaterThan,
            Pageable pageable
            );
    Page<ConcertResponseDto> searchConcertsWithCompatibilityByFestivalId(
            UUID userId,
            UUID festivalId,
            Optional<String> artistName,
            Float minCompatibility,
            Float hasCompatibilityGreaterThan,
            Pageable pageable
    );
    void initCompatibilities(UUID userId);
}
