package com.maco.followthebeat.service.entities;

import com.maco.followthebeat.dto.ArtistDto;
import com.maco.followthebeat.entity.Artist;
import com.maco.followthebeat.mapper.ArtistMapper;
import com.maco.followthebeat.repo.ArtistRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArtistService {
    private final ArtistRepo artistRepo;
    private final ArtistMapper artistMapper;

    public List<ArtistDto> getAllArtists() {
        return artistRepo.findAll().stream()
                .map(artistMapper::toDto)
                .collect(Collectors.toList());
    }

    public ArtistDto getArtistById(UUID id) {
        return artistRepo.findById(id)
                .map(artistMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Artist not found"));
    }

    public ArtistDto createArtist(Artist artist) {
        Artist savedArtist = artistRepo.save(artist);
        return artistMapper.toDto(savedArtist);
    }

    public ArtistDto updateArtist(UUID id, Artist artist) {
        if (!artistRepo.existsById(id)) {
            throw new RuntimeException("Artist not found");
        }
        artist.setId(id);
        Artist updatedArtist = artistRepo.save(artist);
        return artistMapper.toDto(updatedArtist);
    }
} 