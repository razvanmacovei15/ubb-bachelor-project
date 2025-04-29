package com.maco.followthebeat.v2.spotify.tracks.strategy;

import com.maco.followthebeat.v2.spotify.enums.SpotifyTimeRange;
import com.maco.followthebeat.v2.spotify.tracks.entity.BaseUserTopTrack;
import com.maco.followthebeat.v2.spotify.tracks.entity.DbSpotifyTrack;
import com.maco.followthebeat.v2.user.entity.User;

import java.util.List;

public interface TracksSaveStrategy {
    void deleteByUser(User user);
    void saveTrack(User user, DbSpotifyTrack track, int rank);
    List<? extends BaseUserTopTrack> findAllByUser(User user);
    void updateStats(User user);
    SpotifyTimeRange getTimeRange();
}
