package com.maco.followthebeat.v2.core.service.interfaces;

import com.maco.followthebeat.v2.core.dto.ArtistForSuggestionDto;
import com.maco.followthebeat.v2.core.entity.Artist;
import com.maco.followthebeat.v2.core.generics.BaseCrudService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ArtistService extends BaseCrudService<Artist> {
    Optional<Artist> findByName(String name);
    List<Artist> findAllArtistsByFestivalId(UUID festivalId);
    Artist fetchDetailsFromSpotify(Artist artist, UUID userId);
    List<Artist> getAll();
    List<ArtistForSuggestionDto> generateFestivalPayload(UUID festivalId);
    List<ArtistForSuggestionDto> generateAllPayload();
}