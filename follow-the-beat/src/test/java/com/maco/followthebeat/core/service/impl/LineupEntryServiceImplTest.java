package com.maco.followthebeat.core.service.impl;

import com.maco.followthebeat.v2.core.dto.LineupEntryDTO;
import com.maco.followthebeat.v2.core.entity.Concert;
import com.maco.followthebeat.v2.core.entity.LineupEntry;
import com.maco.followthebeat.v2.core.repo.LineupEntryRepo;
import com.maco.followthebeat.v2.core.service.impl.LineupEntryServiceImpl;
import com.maco.followthebeat.v2.core.service.interfaces.ConcertService;
import com.maco.followthebeat.v2.user.entity.User;
import com.maco.followthebeat.v2.user.service.interfaces.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LineupEntryServiceImplTest {

    @Mock
    private LineupEntryRepo lineupEntryRepo;

    @Mock
    private UserService userService;

    @Mock
    private ConcertService concertService;

    @InjectMocks
    private LineupEntryServiceImpl lineupEntryService;

    private User user;
    private Concert concert;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(UUID.randomUUID());

        concert = new Concert();
        concert.setId(UUID.randomUUID());
    }

    @Test
    void createLineupEntry_success() {
        LineupEntryDTO dto = new LineupEntryDTO();
        dto.setUserId(user.getId());
        dto.setConcertId(concert.getId());
        dto.setNotes("Looking forward to this!");
        dto.setPriority(1);
        dto.setCompatibility(90);

        when(userService.findUserById(user.getId())).thenReturn(Optional.of(user));
        when(concertService.getById(concert.getId())).thenReturn(Optional.of(concert));
        when(lineupEntryRepo.existsByUserIdAndConcertId(user.getId(), concert.getId())).thenReturn(false);
        when(lineupEntryRepo.save(any(LineupEntry.class))).thenAnswer(invocation -> invocation.getArgument(0));

        LineupEntry saved = lineupEntryService.createLineupEntry(dto);

        assertNotNull(saved);
        assertEquals(user, saved.getUser());
        assertEquals(concert, saved.getConcert());
        assertEquals("Looking forward to this!", saved.getNotes());
        verify(lineupEntryRepo).save(any(LineupEntry.class));
    }

    @Test
    void createLineupEntry_duplicateThrows() {
        LineupEntryDTO dto = new LineupEntryDTO();
        dto.setUserId(user.getId());
        dto.setConcertId(concert.getId());

        when(userService.findUserById(user.getId())).thenReturn(Optional.of(user));
        when(concertService.getById(concert.getId())).thenReturn(Optional.of(concert));
        when(lineupEntryRepo.existsByUserIdAndConcertId(user.getId(), concert.getId())).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> lineupEntryService.createLineupEntry(dto));
    }

    @Test
    void createLineupEntry_invalidUserThrows() {
        LineupEntryDTO dto = new LineupEntryDTO();
        dto.setUserId(UUID.randomUUID());
        dto.setConcertId(concert.getId());

        when(userService.findUserById(dto.getUserId())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> lineupEntryService.createLineupEntry(dto));
    }

    @Test
    void createLineupEntry_invalidConcertThrows() {
        LineupEntryDTO dto = new LineupEntryDTO();
        dto.setUserId(user.getId());
        dto.setConcertId(UUID.randomUUID());

        when(userService.findUserById(user.getId())).thenReturn(Optional.of(user));
        when(concertService.getById(dto.getConcertId())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> lineupEntryService.createLineupEntry(dto));
    }
}
