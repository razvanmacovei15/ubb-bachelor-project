package com.maco.followthebeat.v2.spotify.artists.strategy;

import com.maco.followthebeat.v2.spotify.enums.SpotifyTimeRange;
import com.maco.followthebeat.v2.spotify.api.SpotifyApiArtistsService;
import com.maco.followthebeat.v2.spotify.artists.entity.DbSpotifyArtist;
import com.maco.followthebeat.v2.spotify.artists.entity.LongTermArtist;
import com.maco.followthebeat.v2.spotify.artists.mapper.SpotifyArtistMapper;
import com.maco.followthebeat.v2.spotify.artists.repo.LongTermArtistRepository;
import com.maco.followthebeat.v2.spotify.artists.service.interfaces.SpotifyArtistService;
import com.maco.followthebeat.v2.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LongTermArtistSaveStrategy extends AbstractArtistSaveStrategy<LongTermArtist> {

    private final LongTermArtistRepository longTermArtistRepository;

    public LongTermArtistSaveStrategy(
            LongTermArtistRepository longTermArtistRepository,
            SpotifyApiArtistsService spotifyApiArtistsService,
            SpotifyArtistMapper spotifyArtistMapper,
            SpotifyArtistService spotifyArtistService
    ) {
        super(spotifyApiArtistsService, spotifyArtistMapper, spotifyArtistService);
        this.longTermArtistRepository = longTermArtistRepository;
    }

    @Override
    protected JpaRepository<LongTermArtist, UUID> getRepository() {
        return longTermArtistRepository;
    }

    @Override
    public SpotifyTimeRange getTimeRange() {
        return SpotifyTimeRange.LONG_TERM;
    }

    @Override
    protected LongTermArtist createEntity(User user, DbSpotifyArtist artist, int rank) {
        LongTermArtist entity = new LongTermArtist();
        entity.setUser(user);
        entity.setArtist(artist);
        entity.setRank(rank);
        entity.setProfile(user.getUserListeningProfile());
        longTermArtistRepository.save(entity);
        return entity;
    }

    @Override
    protected List<LongTermArtist> findAllByUserOrdered(User user) {
        return longTermArtistRepository.findAllByUserOrderByRank(user);
    }
}
