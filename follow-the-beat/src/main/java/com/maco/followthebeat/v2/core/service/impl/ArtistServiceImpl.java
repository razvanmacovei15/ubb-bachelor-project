package com.maco.followthebeat.v2.core.service.impl;

import com.maco.client.v2.SpotifyClient;
import com.maco.client.v2.model.SpotifyArtist;
import com.maco.followthebeat.v2.core.dto.ConcertForSuggestionDto;
import com.maco.followthebeat.v2.core.entity.Artist;
import com.maco.followthebeat.v2.core.entity.Concert;
import com.maco.followthebeat.v2.core.generics.BaseCrudServiceImpl;
import com.maco.followthebeat.v2.core.mappers.ArtistMapper;
import com.maco.followthebeat.v2.core.repo.ArtistRepo;
import com.maco.followthebeat.v2.core.service.interfaces.ArtistService;
import com.maco.followthebeat.v2.core.service.interfaces.ConcertService;
import com.maco.followthebeat.v2.spotify.auth.client.SpotifyClientManager;
import com.maco.followthebeat.v2.user.context.UserContext;
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


    public ArtistServiceImpl(ArtistRepo artistRepo,
                             SpotifyClientManager spotifyClientManager) {
        super(artistRepo);
        this.artistRepo = artistRepo;
        this.spotifyClientManager = spotifyClientManager;
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




}
