package com.maco.followthebeat.service.spotify.interfaces;

import com.maco.followthebeat.entity.User;
import com.maco.followthebeat.entity.spotify.ShortTermArtist;
import com.maco.followthebeat.entity.spotify.MediumTermArtist;
import com.maco.followthebeat.entity.spotify.LongTermArtist;
import com.maco.followthebeat.entity.spotify.interfaces.BaseUserTopArtist;
import com.maco.followthebeat.enums.SpotifyTimeRange;

import java.util.List;

public interface SpotifyArtistStatsService {
    void updateAllStats(User user);
    void deleteAllStats(User user);
    void fetchAndSaveInitialStats(User user);
    List<? extends BaseUserTopArtist> getArtistStatsByTimeRange(User user, SpotifyTimeRange timeRange);
    void updateStatsByTimeRange(User user, SpotifyTimeRange timeRange);
} 