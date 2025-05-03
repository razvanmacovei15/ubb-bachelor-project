package com.maco.followthebeat.base.service.impl;

import com.maco.followthebeat.v2.core.entity.Artist;
import com.maco.followthebeat.v2.core.entity.Concert;
import com.maco.followthebeat.v2.core.entity.Location;
import com.maco.followthebeat.v2.core.mappers.ConcertMapper;
import com.maco.followthebeat.v2.core.repo.ConcertRepo;
import com.maco.followthebeat.v2.core.service.impl.ConcertServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConcertServiceImplTest {

    private ConcertRepo concertRepo;
    private ConcertServiceImpl concertService;
    private Artist testArtist;
    private Location testLocation;
    private ConcertMapper concertMapper;

    @BeforeEach
    void setUp() {
        concertRepo = mock(ConcertRepo.class);
        concertService = new ConcertServiceImpl(concertRepo, concertMapper);

        // Set up a test Artist and Location since they're required in Concert
        testArtist = Artist.builder()
                .id(UUID.randomUUID())
                .name("Test Artist")
                .imgUrl("http://test.com/artist.jpg")
                .genres(List.of("Pop"))
                .build();

        testLocation = mock(Location.class); // Can be mocked or built if needed
    }

    private Concert createTestConcert() {
        return Concert.builder()
                .id(UUID.randomUUID())
                .artist(testArtist)
                .location(testLocation)
                .build();
    }

    @Test
    void testCreateConcert() {
        Concert concert = createTestConcert();
        when(concertRepo.save(any(Concert.class))).thenReturn(concert);

        Concert created = concertService.save(concert);
        assertNotNull(created);
        assertEquals(testArtist.getName(), created.getArtist().getName());
    }

    @Test
    void testUpdateConcertFound() {
        UUID id = UUID.randomUUID();
        Concert updatedConcert = createTestConcert();
        updatedConcert.setId(id);

        when(concertRepo.existsById(id)).thenReturn(true);
        when(concertRepo.save(updatedConcert)).thenReturn(updatedConcert);

        Optional<Concert> result = concertService.update(id, updatedConcert);
        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
    }

    @Test
    void testUpdateConcertNotFound() {
        UUID id = UUID.randomUUID();
        Concert concert = createTestConcert();

        when(concertRepo.existsById(id)).thenReturn(false);

        Optional<Concert> result = concertService.update(id, concert);
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
        Concert concert = createTestConcert();
        concert.setId(id);

        when(concertRepo.findById(id)).thenReturn(Optional.of(concert));

        Optional<Concert> result = concertService.getById(id);
        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
    }

    @Test
    void testGetAll() {
        when(concertRepo.findAll()).thenReturn(List.of(createTestConcert(), createTestConcert()));
        List<Concert> all = concertService.getAll();
        assertEquals(2, all.size());
    }
}
