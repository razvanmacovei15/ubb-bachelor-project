package com.maco.followthebeat.controller;

import com.maco.followthebeat.dto.ArtistDto;
import com.maco.followthebeat.entity.Artist;
import com.maco.followthebeat.service.entities.ArtistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/artists")
@RequiredArgsConstructor
public class ArtistController {
    private final ArtistService artistService;

    @GetMapping
    public ResponseEntity<List<ArtistDto>> getAllArtists() {
        return ResponseEntity.ok(artistService.getAllArtists());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArtistDto> getArtistById(@PathVariable UUID id) {
        return ResponseEntity.ok(artistService.getArtistById(id));
    }

    @PostMapping
    public ResponseEntity<ArtistDto> createArtist(@RequestBody Artist artist) {
        return ResponseEntity.ok(artistService.createArtist(artist));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArtistDto> updateArtist(@PathVariable UUID id, @RequestBody Artist artist) {
        return ResponseEntity.ok(artistService.updateArtist(id, artist));
    }
} 