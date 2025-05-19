package com.maco.followthebeat.v2.core.service.interfaces;

import com.maco.followthebeat.v2.core.dto.ConcertDTO;
import com.maco.followthebeat.v2.core.dto.ConcertForSuggestionDto;
import com.maco.followthebeat.v2.core.entity.Concert;
import com.maco.followthebeat.v2.core.generics.BaseCrudService;
import com.maco.followthebeat.v2.core.model.ConcertResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConcertService extends BaseCrudService<Concert> {
    Page<Concert> getConcerts(Optional<String> artist, Optional<LocalDate> date, Pageable pageable);
    Page<ConcertResponseDto> convertToDTO(Page<Concert> concerts);
    List<Concert> getConcertsByFestivalId(UUID festivalId);
    void deleteByFestivalId(UUID festivalId);
    void deleteConcertById(UUID concertId);
    Page<ConcertResponseDto> findConcertsByFestivalId(Optional<String> artist,  Optional<LocalDate> date, Pageable pageable, UUID festivalId);
    long countConcertsByArtistName(String artistName);
    List<ConcertForSuggestionDto> generateFestivalPayload(UUID festivalId);

}