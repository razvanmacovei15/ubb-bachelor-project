package com.maco.followthebeat.base.service.impl;

import com.maco.followthebeat.feature.base.entity.Concert;
import com.maco.followthebeat.feature.base.repo.ConcertRepo;
import com.maco.followthebeat.feature.base.service.impl.ConcertServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConcertServiceImplTest {

    private ConcertRepo concertRepo;
    private ConcertServiceImpl concertService;

    @BeforeEach
    void setUp() {
        concertRepo = mock(ConcertRepo.class);
        concertService = new ConcertServiceImpl(concertRepo);
    }

    @Test
    void testCreateConcert() {
        Concert concert = new Concert();
        concert.setName("Test Concert");

        when(concertRepo.save(any(Concert.class))).thenReturn(concert);

        Concert created = concertService.create(concert);
        assertNotNull(created);
        assertEquals("Test Concert", created.getName());
    }

    @Test
    void testUpdateConcertFound() {
        UUID id = UUID.randomUUID();
        Concert updatedConcert = new Concert();
        updatedConcert.setId(id);
        updatedConcert.setName("Updated");

        when(concertRepo.existsById(id)).thenReturn(true);
        when(concertRepo.save(updatedConcert)).thenReturn(updatedConcert);

        Optional<Concert> result = concertService.update(id, updatedConcert);
        assertTrue(result.isPresent());
        assertEquals("Updated", result.get().getName());
    }

    @Test
    void testUpdateConcertNotFound() {
        UUID id = UUID.randomUUID();
        when(concertRepo.existsById(id)).thenReturn(false);

        Optional<Concert> result = concertService.update(id, new Concert());
        assertTrue(result.isEmpty());
    }

    @Test
    void testDeleteConcert() {
        UUID id = UUID.randomUUID();
        when(concertRepo.existsById(id)).thenReturn(true);
        doNothing().when(concertRepo).deleteById(id);

        assertDoesNotThrow(() -> concertService.delete(id));
    }

    @Test
    void testDeleteConcertNotFound() {
        UUID id = UUID.randomUUID();
        when(concertRepo.existsById(id)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> concertService.delete(id));
    }

    @Test
    void testGetById() {
        UUID id = UUID.randomUUID();
        Concert concert = new Concert();
        concert.setId(id);

        when(concertRepo.findById(id)).thenReturn(Optional.of(concert));

        Optional<Concert> result = concertService.getById(id);
        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
    }

    @Test
    void testGetAll() {
        when(concertRepo.findAll()).thenReturn(List.of(new Concert(), new Concert()));
        List<Concert> all = concertService.getAll();
        assertEquals(2, all.size());
    }
}
