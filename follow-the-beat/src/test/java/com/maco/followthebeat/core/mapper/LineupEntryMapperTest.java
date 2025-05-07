package com.maco.followthebeat.core.mapper;

import com.maco.followthebeat.v2.core.dto.LineupEntryDTO;
import com.maco.followthebeat.v2.core.entity.Concert;
import com.maco.followthebeat.v2.core.entity.LineupEntry;
import com.maco.followthebeat.v2.core.mappers.LineupEntryMapper;
import com.maco.followthebeat.v2.user.entity.User;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class LineupEntryMapperTest {
    private final LineupEntryMapper lineupEntryMapper = new LineupEntryMapper();

    @Test
    void testToEntity() {
        UUID userId = UUID.randomUUID();
        UUID concertId = UUID.randomUUID();
        UUID entryId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);

        Concert concert = new Concert();
        concert.setId(concertId);

        LineupEntryDTO dto = new LineupEntryDTO();
        dto.setId(entryId);
        dto.setUserId(userId);
        dto.setConcertId(concertId);
        dto.setNotes("Test note");
        dto.setPriority(2);
        dto.setCompatibility(95);

        LineupEntry entity = lineupEntryMapper.toEntity(dto, user, concert);

        assertEquals(entryId, entity.getId());
        assertEquals(user, entity.getUser());
        assertEquals(concert, entity.getConcert());
        assertEquals("Test note", entity.getNotes());
        assertEquals(2, entity.getPriority());
        assertEquals(95, entity.getCompatibility());
    }

    @Test
    void testToDTO() {
        UUID userId = UUID.randomUUID();
        UUID concertId = UUID.randomUUID();
        UUID entryId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);

        Concert concert = new Concert();
        concert.setId(concertId);

        LineupEntry entity = LineupEntry.builder()
                .id(entryId)
                .user(user)
                .concert(concert)
                .notes("Another note")
                .priority(1)
                .compatibility(88)
                .build();

        LineupEntryDTO dto = lineupEntryMapper.toDTO(entity);

        assertEquals(entryId, dto.getId());
        assertEquals(userId, dto.getUserId());
        assertEquals(concertId, dto.getConcertId());
        assertEquals("Another note", dto.getNotes());
        assertEquals(1, dto.getPriority());
        assertEquals(88, dto.getCompatibility());
    }
}
