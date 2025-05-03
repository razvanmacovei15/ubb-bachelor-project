package com.maco.followthebeat.v2.core.service.interfaces;

import com.maco.followthebeat.v2.core.dto.ConcertDTO;
import com.maco.followthebeat.v2.core.entity.Concert;
import com.maco.followthebeat.v2.core.generics.BaseCrudService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;

public interface ConcertService extends BaseCrudService<Concert> {
    Page<Concert> getConcerts(Optional<String> artist, Optional<String> city, Optional<LocalDate> date, Pageable pageable);
    Page<ConcertDTO> convertToDTO(Page<Concert> concerts);

}