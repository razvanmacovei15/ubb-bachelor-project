package com.maco.followthebeat.v2.core.service.impl;

import com.maco.client.v2.SpotifyClient;
import com.maco.client.v2.model.SpotifyArtist;
import com.maco.followthebeat.v2.core.dto.ArtistForSuggestionDto;
import com.maco.followthebeat.v2.core.entity.Artist;
import com.maco.followthebeat.v2.core.generics.BaseCrudServiceImpl;
import com.maco.followthebeat.v2.core.mappers.ArtistMapper;
import com.maco.followthebeat.v2.core.repo.ArtistRepo;
import com.maco.followthebeat.v2.core.service.interfaces.ArtistService;
import com.maco.followthebeat.v2.spotify.auth.client.SpotifyClientManager;
import com.maco.followthebeat.v2.user.context.UserContext;
import com.maco.followthebeat.v2.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
@Slf4j
@Service
public class ArtistServiceImpl extends BaseCrudServiceImpl<Artist> implements ArtistService {
    private final ArtistRepo artistRepo;
    private final SpotifyClientManager spotifyClientManager;
    private final ArtistMapper artistMapper;
    private final Executor taskExecutor;
    private final UserContext userContext;

    public ArtistServiceImpl(ArtistRepo artistRepo,
                             SpotifyClientManager spotifyClientManager,
                             ArtistMapper artistMapper,
                             @Qualifier("spotifyTaskExecutor") Executor taskExecutor,
                             UserContext userContext) {
        super(artistRepo);
        this.artistRepo = artistRepo;
        this.spotifyClientManager = spotifyClientManager;
        this.artistMapper = artistMapper;
        this.taskExecutor = taskExecutor;
        this.userContext = userContext;
    }


    @Override
    public Optional<Artist> findByName(String name) {
        return artistRepo.findByName(name);
    }

    @Override
    public List<Artist> findAllArtistsByFestivalId(UUID festivalId) {
        return artistRepo.findAllArtistsByFestivalId(festivalId);
    }

    private Artist updateWithSpotifyDetails(Artist artist, SpotifyArtist spotifyArtist) {
        artist.setSpotifyId(spotifyArtist.getId());
        artist.setGenres(List.of(spotifyArtist.getGenres()));
        artist.setSpotifyUrl(spotifyArtist.getExternalUrls().getSpotify());
        return artistRepo.save(artist);
    }

    @Override
    public Artist fetchDetailsFromSpotify(Artist artist, UUID userId) {
        log.info("Fetching details for artist: {}", artist.getName());
        String artistName = artist.getName();
        log.info("Searching for artist: {}", artistName);
        SpotifyClient spotifyClient = spotifyClientManager.getOrCreateSpotifyClient(userId);
        log.info("Client created for user: {}", userId);
        List<SpotifyArtist> spotifyArtistResponse = spotifyClient.searchForArtist(artistName);
        log.info("Found {} artists for name: {}", spotifyArtistResponse.size(), artistName);
        SpotifyArtist spotifyArtist = spotifyArtistResponse.get(0);
        log.info("Updating artist with Spotify details: {}", spotifyArtist.getName());
        return updateWithSpotifyDetails(artist, spotifyArtist);
    }

    private CompletableFuture<ArtistForSuggestionDto> updateAndMapAsync(Artist artist, UUID userId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                log.info("Fetching details for artist: {}", artist.getName());
                Artist updated = fetchDetailsFromSpotify(artist, userId);
                log.info("Fetched details for artist: {}", updated.getName());
                return artistMapper.toSuggestionDto(updated);
            } catch (Exception e) {
                // Log and fall back to mapping existing artist
                return artistMapper.toSuggestionDto(artist);
            }
        }, taskExecutor);
    }

    @Override
    public List<ArtistForSuggestionDto> generateFestivalPayload(UUID festivalId) {
        log.info("Generating festival payload for festival ID: {}", festivalId);
        List<Artist> artists = artistRepo.findAllArtistsByFestivalId(festivalId);
        log.info("Found {} artists for festival ID: {}", artists.size(), festivalId);
        UUID userId = userContext.getOrThrow().getId();
        log.info("User ID: {}", userId);
        List<CompletableFuture<ArtistForSuggestionDto>> futures = artists.stream()
                .map(artist -> updateAndMapAsync(artist, userId))
                .toList();
        log.info("Generated {} futures for artists", futures.size());
        log.info("Waiting for all futures to complete");
        return futures.stream()
                .map(CompletableFuture::join)
                .toList();
    }

    @Override
    public List<ArtistForSuggestionDto> generateAllPayload() {
        List<Artist> artists = artistRepo.findAll();
        UUID userId = userContext.getOrThrow().getId();
        List<CompletableFuture<ArtistForSuggestionDto>> futures = artists.stream()
                .map(artist -> updateAndMapAsync(artist, userId))
                .toList();

        return futures.stream()
                .map(CompletableFuture::join)
                .toList();
    }


}
