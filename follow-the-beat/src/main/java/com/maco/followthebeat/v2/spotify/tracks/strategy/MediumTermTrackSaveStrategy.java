package com.maco.followthebeat.v2.spotify.tracks.strategy;
import com.maco.followthebeat.v2.spotify.tracks.service.interfaces.SpotifyTrackService;
import com.maco.followthebeat.v2.spotify.enums.SpotifyTimeRange;
import com.maco.followthebeat.v2.spotify.api.SpotifyApiTracksService;
import com.maco.followthebeat.v2.spotify.tracks.entity.DbSpotifyTrack;
import com.maco.followthebeat.v2.spotify.tracks.entity.MediumTermTrack;
import com.maco.followthebeat.v2.spotify.tracks.mapper.SpotifyTrackMapper;
import com.maco.followthebeat.v2.spotify.tracks.repo.MediumTermTrackRepository;
import com.maco.followthebeat.v2.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MediumTermTrackSaveStrategy extends AbstractTrackSaveStrategy<MediumTermTrack> {
    private final MediumTermTrackRepository mediumTermTrackRepository;

    public MediumTermTrackSaveStrategy(SpotifyApiTracksService tracksApiService, SpotifyTrackMapper trackMapper, SpotifyTrackService spotifyTrackService, MediumTermTrackRepository mediumTermTrackRepository) {
        super(tracksApiService, trackMapper, spotifyTrackService);
        this.mediumTermTrackRepository = mediumTermTrackRepository;
    }

    @Override
    protected JpaRepository<MediumTermTrack, UUID> getRepo() {
        return mediumTermTrackRepository;
    }

    @Override
    public SpotifyTimeRange getTimeRange() {
        return SpotifyTimeRange.MEDIUM_TERM;
    }

    @Override
    protected MediumTermTrack createEntity(User user, DbSpotifyTrack track, int rank) {
        MediumTermTrack mediumTermTrack = new MediumTermTrack();
        mediumTermTrack.setUser(user);
        mediumTermTrack.setTrack(track);
        mediumTermTrack.setRank(rank);
        mediumTermTrack.setProfile(user.getUserListeningProfile());
        mediumTermTrackRepository.save(mediumTermTrack);
        return mediumTermTrack;
    }

    @Override
    protected List<MediumTermTrack> findAllByUserOrdered(User user) {
        return mediumTermTrackRepository.findAllByUserOrderByRank(user);
    }
}
