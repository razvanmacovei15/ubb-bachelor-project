package com.maco.followthebeat.v2.spotify.tracks.repo;

import com.maco.followthebeat.v2.spotify.tracks.entity.ShortTermTrack;
import com.maco.followthebeat.v2.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ShortTermTrackRepository extends JpaRepository<ShortTermTrack, UUID> {
    List<ShortTermTrack> findAllByUserOrderByRank(User user);
    void deleteByUser(User user);


} 