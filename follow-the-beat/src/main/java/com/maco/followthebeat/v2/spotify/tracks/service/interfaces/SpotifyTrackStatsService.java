package com.maco.followthebeat.v2.spotify.tracks.service.interfaces;

import com.maco.followthebeat.v2.common.enums.SpotifyTimeRange;
import com.maco.followthebeat.v2.spotify.artists.dto.SpotifyArtistDto;
import com.maco.followthebeat.v2.spotify.artists.entity.BaseUserTopArtist;
import com.maco.followthebeat.v2.spotify.tracks.dto.SpotifyTrackDto;
import com.maco.followthebeat.v2.user.entity.User;

import java.util.List;

public interface SpotifyTrackStatsService {
    void updateAllStats(User user);
    void deleteAllStats(User user);
    List<SpotifyTrackDto> fetchAndSaveInitialStats(User user, SpotifyTimeRange timeRange);
    List<? extends BaseUserTopArtist> getArtistStatsByTimeRange(User user, SpotifyTimeRange timeRange);
    void updateStatsByTimeRange(User user, SpotifyTimeRange timeRange);
    List<SpotifyArtistDto> getTopArtistsByTimeRange(User user, SpotifyTimeRange timeRange);
}
