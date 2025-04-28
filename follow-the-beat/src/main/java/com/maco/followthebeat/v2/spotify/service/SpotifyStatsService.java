package com.maco.followthebeat.v2.spotify.service;

import com.maco.followthebeat.v2.common.enums.SpotifyTimeRange;
import com.maco.followthebeat.v2.spotify.artists.dto.SpotifyArtistDto;
import com.maco.followthebeat.v2.spotify.artists.entity.BaseUserTopArtist;
import com.maco.followthebeat.v2.user.entity.User;

import java.util.List;

public interface SpotifyStatsService<T, E> {
    void updateAllStats(User user);
    void deleteAllStats(User user);
    List<T> fetchAndSaveInitialStats(User user, SpotifyTimeRange timeRange);
    List<E> getStatsByTimeRange(User user, SpotifyTimeRange timeRange);
    void updateStatsByTimeRange(User user, SpotifyTimeRange timeRange);
    List<T> getTopByTimeRange(User user, SpotifyTimeRange timeRange);
}
