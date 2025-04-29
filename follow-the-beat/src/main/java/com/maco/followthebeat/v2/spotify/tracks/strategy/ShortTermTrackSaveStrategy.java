package com.maco.followthebeat.v2.spotify.tracks.strategy;
import com.maco.followthebeat.v2.spotify.tracks.service.interfaces.SpotifyTrackService;
import com.maco.followthebeat.v2.spotify.enums.SpotifyTimeRange;
import com.maco.followthebeat.v2.spotify.api.SpotifyApiTracksService;
import com.maco.followthebeat.v2.spotify.tracks.entity.DbSpotifyTrack;
import com.maco.followthebeat.v2.spotify.tracks.entity.ShortTermTrack;
import com.maco.followthebeat.v2.spotify.tracks.mapper.SpotifyTrackMapper;
import com.maco.followthebeat.v2.spotify.tracks.repo.ShortTermTrackRepository;
import com.maco.followthebeat.v2.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
@Service
public class ShortTermTrackSaveStrategy extends AbstractTrackSaveStrategy<ShortTermTrack>{
    private final ShortTermTrackRepository shortTermTrackRepository;

    public ShortTermTrackSaveStrategy(SpotifyApiTracksService tracksApiService, SpotifyTrackMapper trackMapper, SpotifyTrackService spotifyTrackService, ShortTermTrackRepository shortTermTrackRepository) {
        super(tracksApiService, trackMapper, spotifyTrackService);
        this.shortTermTrackRepository = shortTermTrackRepository;
    }

    @Override
    protected JpaRepository<ShortTermTrack, UUID> getRepo() {
        return shortTermTrackRepository;
    }

    @Override
    public SpotifyTimeRange getTimeRange() {
        return SpotifyTimeRange.SHORT_TERM;
    }

    @Override
    protected ShortTermTrack createEntity(User user, DbSpotifyTrack track, int rank) {
        ShortTermTrack shortTermTrack = new ShortTermTrack();
        shortTermTrack.setUser(user);
        shortTermTrack.setTrack(track);
        shortTermTrack.setRank(rank);
        return shortTermTrack;
    }

    @Override
    protected List<ShortTermTrack> findAllByUserOrdered(User user) {
        return shortTermTrackRepository.findAllByUserOrderByRank(user);
    }
}
