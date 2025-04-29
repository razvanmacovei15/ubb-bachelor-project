package com.maco.followthebeat.v2.spotify.tracks.service.interfaces;

import com.maco.followthebeat.v2.spotify.enums.SpotifyTimeRange;
import com.maco.followthebeat.v2.spotify.tracks.dto.SpotifyTrackDto;
import com.maco.followthebeat.v2.spotify.tracks.entity.BaseUserTopTrack;
import com.maco.followthebeat.v2.user.entity.User;

import java.util.List;

public interface SpotifyTrackStatsService {
    void updateAllStats(User user);
    void deleteAllStats(User user);
    List<SpotifyTrackDto> fetchAndSaveInitialStats(User user, SpotifyTimeRange timeRange);
    List<? extends BaseUserTopTrack> getTrackStatsByTimeRange(User user, SpotifyTimeRange timeRange);
    void updateStatsByTimeRange(User user, SpotifyTimeRange timeRange);
    List<SpotifyTrackDto> getTopTracksByTimeRange(User user, SpotifyTimeRange timeRange);
}
