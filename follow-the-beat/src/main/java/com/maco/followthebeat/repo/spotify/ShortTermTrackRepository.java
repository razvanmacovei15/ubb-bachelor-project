package com.maco.followthebeat.repo.spotify;

import com.maco.followthebeat.entity.User;
import com.maco.followthebeat.entity.spotify.ShortTermTrack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ShortTermTrackRepository extends JpaRepository<ShortTermTrack, UUID> {
    List<ShortTermTrack> findAllByUserOrderByRank(User user);
    void deleteByUser(User user);


} 