package com.maco.followthebeat.v2.spotify.tracks.strategy;

import com.maco.followthebeat.v2.common.enums.SpotifyTimeRange;
import com.maco.followthebeat.v2.spotify.api.SpotifyApiTracksService;
import com.maco.followthebeat.v2.spotify.tracks.dto.SpotifyTrackDto;
import com.maco.followthebeat.v2.spotify.tracks.entity.BaseUserTopTrack;
import com.maco.followthebeat.v2.spotify.tracks.entity.DbSpotifyTrack;
import com.maco.followthebeat.v2.spotify.tracks.mapper.SpotifyTrackMapper;
import com.maco.followthebeat.v2.spotify.tracks.service.interfaces.SpotifyTrackService;
import com.maco.followthebeat.v2.user.entity.User;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public abstract class AbstractTrackSaveStrategy<T extends BaseUserTopTrack> implements TracksSaveStrategy {
    protected final SpotifyApiTracksService tracksApiService;
    protected final SpotifyTrackMapper trackMapper;
    protected final SpotifyTrackService spotifyTrackService;

    protected abstract JpaRepository<T, UUID> getRepo();
    protected abstract T createEntity(User user, DbSpotifyTrack track, int rank);
    protected abstract List<T> findAllByUserOrdered(User user);
    @Override
    public void deleteByUser(User user) {
        findAllByUserOrdered(user).forEach(e -> getRepo().delete(e));
    }

    @Override
    public void saveTrack(User user, DbSpotifyTrack track, int rank) {
        T entity = createEntity(user, track, rank);
        getRepo().save(entity);
    }

    @Override
    public List<T> findAllByUser(User user) {
        return findAllByUserOrdered(user);
    }

    @Override
    public void updateStats(User user) {
        List<SpotifyTrackDto> tracks = null;
        if(spotifyTrackService != null){
            tracks = tracksApiService.fetchTopItems(
                    user.getId(),
                    getTimeRange()
                    ,50
                    ,0
            );
        }
        if(tracks !=  null){
            saveTracks(user, tracks);
        }
    }

    private void saveTracks(User user, List<SpotifyTrackDto> tracks) {
        deleteByUser(user);
        List<T> newTracks = IntStream.range(0, tracks.size())
                .mapToObj(index -> {
                    SpotifyTrackDto clientTrack =  tracks.get(index);
                    DbSpotifyTrack dbSpotifyTrack = null;
                    if(trackMapper != null){
                        dbSpotifyTrack = trackMapper.toDbSpotifyTrack(clientTrack);
                    }
                    DbSpotifyTrack managedTrack = null;
                    if(spotifyTrackService != null){
                        managedTrack = spotifyTrackService.saveOrGetExistingTrack(dbSpotifyTrack);
                    }
                    return createEntity(user, managedTrack, index + 1);
                }).toList();
        getRepo().saveAll(newTracks);
    }


}
