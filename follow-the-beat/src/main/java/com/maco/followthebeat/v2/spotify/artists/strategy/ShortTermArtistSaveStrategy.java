package com.maco.followthebeat.v2.spotify.artists.strategy;

import com.maco.followthebeat.v2.spotify.artists.entity.ShortTermArtist;
import com.maco.followthebeat.v2.spotify.artists.repo.ShortTermArtistRepository;
import com.maco.followthebeat.v2.spotify.api.SpotifyApiArtistsService;
import com.maco.followthebeat.v2.spotify.artists.mapper.SpotifyArtistMapper;
import com.maco.followthebeat.v2.spotify.artists.service.interfaces.SpotifyArtistService;
import com.maco.followthebeat.v2.user.entity.User;
import com.maco.followthebeat.v2.common.enums.SpotifyTimeRange;
import com.maco.followthebeat.v2.spotify.artists.entity.DbSpotifyArtist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ShortTermArtistSaveStrategy extends AbstractArtistSaveStrategy<ShortTermArtist> {

    private final ShortTermArtistRepository shortTermArtistRepository;

    public ShortTermArtistSaveStrategy(
            ShortTermArtistRepository shortTermArtistRepository,
            SpotifyApiArtistsService spotifyApiArtistsService,
            SpotifyArtistMapper spotifyArtistMapper,
            SpotifyArtistService spotifyArtistService
    ) {
        super(spotifyApiArtistsService, spotifyArtistMapper, spotifyArtistService);
        this.shortTermArtistRepository = shortTermArtistRepository;
    }

    @Override
    protected JpaRepository<ShortTermArtist, UUID> getRepository() {
        return shortTermArtistRepository;
    }

    @Override
    protected SpotifyTimeRange getTimeRange() {
        return SpotifyTimeRange.SHORT_TERM;
    }

    @Override
    protected ShortTermArtist createEntity(User user, DbSpotifyArtist artist, int rank) {
        ShortTermArtist entity = new ShortTermArtist();
        entity.setUser(user);
        entity.setArtist(artist);
        entity.setRank(rank);
        return entity;
    }

    @Override
    protected List<ShortTermArtist> findAllByUserOrdered(User user) {
        return shortTermArtistRepository.findAllByUserOrderByRank(user);
    }
}
