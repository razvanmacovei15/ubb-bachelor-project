package com.maco.followthebeat.v2.spotify.tracks.strategy;

import com.maco.followthebeat.v2.spotify.enums.SpotifyTimeRange;
import com.maco.followthebeat.v2.spotify.tracks.service.interfaces.SpotifyTrackService;
import com.maco.followthebeat.v2.spotify.api.SpotifyApiTracksService;
import com.maco.followthebeat.v2.spotify.tracks.entity.DbSpotifyTrack;
import com.maco.followthebeat.v2.spotify.tracks.entity.LongTermTrack;
import com.maco.followthebeat.v2.spotify.tracks.mapper.SpotifyTrackMapper;
import com.maco.followthebeat.v2.spotify.tracks.repo.LongTermTrackRepository;
import com.maco.followthebeat.v2.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LongTermTrackSaveStrategy extends AbstractTrackSaveStrategy<LongTermTrack> {
    private final LongTermTrackRepository longTermTrackRepository;

    public LongTermTrackSaveStrategy(SpotifyApiTracksService tracksApiService, SpotifyTrackMapper trackMapper, SpotifyTrackService spotifyTrackService, LongTermTrackRepository longTermTrackRepository) {
        super(tracksApiService, trackMapper, spotifyTrackService);
        this.longTermTrackRepository = longTermTrackRepository;
    }

    @Override
    protected JpaRepository<LongTermTrack, UUID> getRepo() {
        return longTermTrackRepository;
    }

    @Override
    public SpotifyTimeRange getTimeRange() {
        return SpotifyTimeRange.LONG_TERM;
    }

    @Override
    protected LongTermTrack createEntity(User user, DbSpotifyTrack track, int rank) {
        LongTermTrack longTermTrack = new LongTermTrack();
        longTermTrack.setUser(user);
        longTermTrack.setTrack(track);
        longTermTrack.setRank(rank);
        return longTermTrack;
    }

    @Override
    protected List<LongTermTrack> findAllByUserOrdered(User user) {
        return longTermTrackRepository.findAllByUserOrderByRank(user);
    }
}
