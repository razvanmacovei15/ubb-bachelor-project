package com.maco.followthebeat.v2.core.mappers;

import com.maco.followthebeat.v2.core.dto.LineupEntryDTO;
import com.maco.followthebeat.v2.core.entity.Concert;
import com.maco.followthebeat.v2.core.entity.ConcertCompatibility;
import com.maco.followthebeat.v2.core.entity.LineupEntry;
import com.maco.followthebeat.v2.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class LineupEntryMapper {
    public LineupEntry toEntity(LineupEntryDTO dto, ConcertCompatibility concertCompatibility) {
        return LineupEntry.builder()
                .id(dto.getId())
                .concertCompatibility(concertCompatibility)
                .notes(dto.getNotes())
                .priority(dto.getPriority())
                .build();
    }

    public LineupEntryDTO toDTO(LineupEntry entity) {
        LineupEntryDTO dto = new LineupEntryDTO();
        dto.setId(entity.getId());
        dto.setConcertId(entity.getConcertCompatibility().getConcert().getId());
        dto.setNotes(entity.getNotes());
        dto.setPriority(entity.getPriority());
        return dto;
    }
}
