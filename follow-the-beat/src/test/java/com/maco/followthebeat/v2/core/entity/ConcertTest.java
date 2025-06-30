package com.maco.followthebeat.v2.core.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@DisplayName("Concert Entity Tests")
class ConcertTest {

    private Concert concert;
    private Artist artist;
    private Location location;
    private Schedule schedule;

    @BeforeEach
    void setUp() {
        // Create test artist
        artist = Artist.builder()
                .id(UUID.randomUUID())
                .name("Test Artist")
                .imgUrl("https://example.com/artist.jpg")
                .spotifyId("spotify:artist:123")
                .spotifyUrl("https://open.spotify.com/artist/123")
                .build();

        // Create test location (Venue) using constructor
        Venue testVenue = new Venue();
        testVenue.setId(UUID.randomUUID());
        testVenue.setName("Test Venue");
        testVenue.setImgUrl("https://example.com/venue.jpg");
        testVenue.setAddress("123 Test Street");
        testVenue.setCity("Test City");
        testVenue.setCountry("Test Country");
        testVenue.setCapacity(1000);
        location = testVenue;

        // Create test schedule
        schedule = Schedule.builder()
                .id(UUID.randomUUID())
                .date(LocalDate.now().plusDays(30))
                .startTime(LocalTime.of(20, 0))
                .build();

        // Create test concert
        concert = Concert.builder()
                .id(UUID.randomUUID())
                .artist(artist)
                .location(location)
                .schedule(schedule)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        // Set up bidirectional relationship
        schedule.setConcert(concert);
    }

    @Test
    @DisplayName("Should create concert with all required fields")
    void shouldCreateConcertWithAllRequiredFields() {
        assertNotNull(concert);
        assertNotNull(concert.getId());
        assertNotNull(concert.getArtist());
        assertNotNull(concert.getLocation());
        assertNotNull(concert.getSchedule());
        assertEquals(artist, concert.getArtist());
        assertEquals(location, concert.getLocation());
        assertEquals(schedule, concert.getSchedule());
    }

    @Test
    @DisplayName("Should have correct artist relationship")
    void shouldHaveCorrectArtistRelationship() {
        assertEquals(artist.getName(), concert.getArtist().getName());
        assertEquals(artist.getSpotifyId(), concert.getArtist().getSpotifyId());
        assertEquals(artist.getImgUrl(), concert.getArtist().getImgUrl());
    }

    @Test
    @DisplayName("Should have correct location relationship")
    void shouldHaveCorrectLocationRelationship() {
        assertEquals(location.getName(), concert.getLocation().getName());
        assertEquals(location.getImgUrl(), concert.getLocation().getImgUrl());

        // Test that it's a Venue
        assertTrue(concert.getLocation() instanceof Venue);
        Venue venue = (Venue) concert.getLocation();
        assertEquals("Test Venue", venue.getName());
        assertEquals("Test City", venue.getCity());
        assertEquals("Test Country", venue.getCountry());
        assertEquals(1000, venue.getCapacity());
    }

    @Test
    @DisplayName("Should have correct schedule relationship")
    void shouldHaveCorrectScheduleRelationship() {
        assertEquals(schedule.getDate(), concert.getSchedule().getDate());
        assertEquals(schedule.getStartTime(), concert.getSchedule().getStartTime());
        assertEquals(concert, schedule.getConcert()); // Bidirectional relationship
    }

    @Test
    @DisplayName("Should have timestamps")
    void shouldHaveTimestamps() {
        assertNotNull(concert.getCreatedAt());
        assertNotNull(concert.getUpdatedAt());
        assertTrue(concert.getCreatedAt().isBefore(Instant.now().plusSeconds(1)));
        assertTrue(concert.getUpdatedAt().isBefore(Instant.now().plusSeconds(1)));
    }

    @Test
    @DisplayName("Should work with Stage location")
    void shouldWorkWithStageLocation() {
        // Create a Festival first
        Festival festival = Festival.builder()
                .id(UUID.randomUUID())
                .name("Test Festival")
                .description("A test festival")
                .location("Test Location")
                .startDate(LocalDate.now().plusDays(10))
                .endDate(LocalDate.now().plusDays(15))
                .isActive(true)
                .build();

        // Create a Stage using constructor
        Stage stage = new Stage();
        stage.setId(UUID.randomUUID());
        stage.setName("Main Stage");
        stage.setImgUrl("https://example.com/stage.jpg");
        stage.setFestival(festival);

        // Create concert with stage
        Concert stageConcert = Concert.builder()
                .id(UUID.randomUUID())
                .artist(artist)
                .location(stage)
                .schedule(schedule)
                .build();

        assertNotNull(stageConcert);
        assertTrue(stageConcert.getLocation() instanceof Stage);
        Stage concertStage = (Stage) stageConcert.getLocation();
        assertEquals("Main Stage", concertStage.getName());
        assertEquals(festival, concertStage.getFestival());
    }

    @Test
    @DisplayName("Should handle null location")
    void shouldHandleNullLocation() {
        Concert concertWithoutLocation = Concert.builder()
                .id(UUID.randomUUID())
                .artist(artist)
                .location(null)
                .schedule(schedule)
                .build();

        assertNotNull(concertWithoutLocation);
        assertNull(concertWithoutLocation.getLocation());
        assertNotNull(concertWithoutLocation.getArtist());
        assertNotNull(concertWithoutLocation.getSchedule());
    }

    @Test
    @DisplayName("Should handle null schedule")
    void shouldHandleNullSchedule() {
        Concert concertWithoutSchedule = Concert.builder()
                .id(UUID.randomUUID())
                .artist(artist)
                .location(location)
                .schedule(null)
                .build();

        assertNotNull(concertWithoutSchedule);
        assertNull(concertWithoutSchedule.getSchedule());
        assertNotNull(concertWithoutSchedule.getArtist());
        assertNotNull(concertWithoutSchedule.getLocation());
    }

    @Test
    @DisplayName("Should update timestamps correctly")
    void shouldUpdateTimestampsCorrectly() {
        Instant originalCreatedAt = concert.getCreatedAt();
        Instant originalUpdatedAt = concert.getUpdatedAt();

        // Simulate some time passing
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Update the concert
        concert.setUpdatedAt(Instant.now());

        assertEquals(originalCreatedAt, concert.getCreatedAt()); // Created at should not change
        assertTrue(concert.getUpdatedAt().isAfter(originalUpdatedAt)); // Updated at should change
    }

    @Test
    @DisplayName("Should work with Lombok annotations")
    void shouldWorkWithLombokAnnotations() {
        // Test that Lombok annotations work correctly
        Concert testConcert = new Concert();
        testConcert.setId(UUID.randomUUID());
        testConcert.setArtist(artist);
        testConcert.setLocation(location);
        testConcert.setSchedule(schedule);

        assertNotNull(testConcert.getId());
        assertEquals(artist, testConcert.getArtist());
        assertEquals(location, testConcert.getLocation());
        assertEquals(schedule, testConcert.getSchedule());
    }


}