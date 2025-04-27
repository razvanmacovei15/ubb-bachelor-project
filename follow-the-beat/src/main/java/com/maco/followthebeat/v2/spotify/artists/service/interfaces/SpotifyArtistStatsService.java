package com.maco.followthebeat.v2.spotify.artists.service.interfaces;

import com.maco.followthebeat.v2.spotify.artists.dto.SpotifyArtistDto;
import com.maco.followthebeat.v2.user.entity.User;
import com.maco.followthebeat.v2.spotify.artists.entity.BaseUserTopArtist;
import com.maco.followthebeat.v2.common.enums.SpotifyTimeRange;

import java.util.List;

public interface SpotifyArtistStatsService {
    void updateAllStats(User user);
    void deleteAllStats(User user);
    List<SpotifyArtistDto> fetchAndSaveInitialStats(User user, SpotifyTimeRange timeRange);
    List<? extends BaseUserTopArtist> getArtistStatsByTimeRange(User user, SpotifyTimeRange timeRange);
    void updateStatsByTimeRange(User user, SpotifyTimeRange timeRange);
    List<SpotifyArtistDto> getTopArtistsByTimeRange(User user, SpotifyTimeRange timeRange);
} 