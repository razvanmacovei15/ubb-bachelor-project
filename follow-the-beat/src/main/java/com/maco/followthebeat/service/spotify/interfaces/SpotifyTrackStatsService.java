package com.maco.followthebeat.service.spotify.interfaces;

import com.maco.followthebeat.entity.User;
import com.maco.followthebeat.entity.spotify.ShortTermTrack;
import com.maco.followthebeat.entity.spotify.MediumTermTrack;
import com.maco.followthebeat.entity.spotify.LongTermTrack;
import java.util.List;

public interface SpotifyTrackStatsService {
    // Short term stats
    List<ShortTermTrack> getShortTermTracks(User user);
    void updateShortTermStats(User user);

    // Medium term stats
    List<MediumTermTrack> getMediumTermTracks(User user);
    void updateMediumTermStats(User user);

    // Long term stats
    List<LongTermTrack> getLongTermTracks(User user);
    void updateLongTermStats(User user);

    // Combined operations
    void updateAllStats(User user);
    void deleteAllStats(User user);
    
    // Initial data fetch
    void fetchAndSaveInitialStats(User user);
} 