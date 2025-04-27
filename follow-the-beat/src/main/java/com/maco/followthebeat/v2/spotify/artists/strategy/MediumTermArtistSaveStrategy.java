package com.maco.followthebeat.v2.spotify.artists.strategy;

import com.maco.followthebeat.v2.spotify.artists.entity.MediumTermArtist;
import com.maco.followthebeat.v2.spotify.artists.repo.MediumTermArtistRepository;
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
public class MediumTermArtistSaveStrategy extends AbstractArtistSaveStrategy<MediumTermArtist> {

    private final MediumTermArtistRepository mediumTermArtistRepository;

    public MediumTermArtistSaveStrategy(
            MediumTermArtistRepository mediumTermArtistRepository,
            SpotifyApiArtistsService spotifyApiArtistsService,
            SpotifyArtistMapper spotifyArtistMapper,
            SpotifyArtistService spotifyArtistService
    ) {
        super(spotifyApiArtistsService, spotifyArtistMapper, spotifyArtistService);
        this.mediumTermArtistRepository = mediumTermArtistRepository;
    }

    @Override
    protected JpaRepository<MediumTermArtist, UUID> getRepository() {
        return mediumTermArtistRepository;
    }

    @Override
    protected SpotifyTimeRange getTimeRange() {
        return SpotifyTimeRange.MEDIUM_TERM;
    }

    @Override
    protected MediumTermArtist createEntity(User user, DbSpotifyArtist artist, int rank) {
        MediumTermArtist entity = new MediumTermArtist();
        entity.setUser(user);
        entity.setArtist(artist);
        entity.setRank(rank);
        return entity;
    }

    @Override
    protected List<MediumTermArtist> findAllByUserOrdered(User user) {
        return mediumTermArtistRepository.findAllByUserOrderByRank(user);
    }
}
