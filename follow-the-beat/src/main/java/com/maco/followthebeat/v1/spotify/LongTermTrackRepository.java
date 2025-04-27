package com.maco.followthebeat.v1.spotify;

import com.maco.followthebeat.v2.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LongTermTrackRepository extends JpaRepository<LongTermTrack, UUID> {
    List<LongTermTrack> findAllByUserOrderByRank(User user);
    void deleteByUser(User user);
} 