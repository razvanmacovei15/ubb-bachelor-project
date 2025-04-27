package com.maco.followthebeat.service.savestrategy;

import com.maco.followthebeat.entity.User;
import com.maco.followthebeat.entity.spotify.DbSpotifyArtist;
import com.maco.followthebeat.entity.spotify.interfaces.BaseUserTopArtist;

import java.util.List;

public interface ArtistSaveStrategy {
    void deleteByUser(User user);
    void saveArtist(User user, DbSpotifyArtist artist, int rank);
    List<? extends BaseUserTopArtist> findAllByUser(User user);
    void updateStats(User user);
}
