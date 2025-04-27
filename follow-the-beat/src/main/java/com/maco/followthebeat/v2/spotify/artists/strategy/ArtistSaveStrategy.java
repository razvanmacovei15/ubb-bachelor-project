package com.maco.followthebeat.v2.spotify.artists.strategy;

import com.maco.followthebeat.v2.user.entity.User;
import com.maco.followthebeat.v2.spotify.artists.entity.DbSpotifyArtist;
import com.maco.followthebeat.v2.spotify.artists.entity.BaseUserTopArtist;

import java.util.List;

public interface ArtistSaveStrategy {
    void deleteByUser(User user);
    void saveArtist(User user, DbSpotifyArtist artist, int rank);
    List<? extends BaseUserTopArtist> findAllByUser(User user);
    void updateStats(User user);
}
