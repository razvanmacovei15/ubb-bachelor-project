package com.maco.followthebeat.repo.spotify;

import com.maco.followthebeat.v2.user.entity.User;
import com.maco.followthebeat.entity.spotify.MediumTermTrack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MediumTermTrackRepository extends JpaRepository<MediumTermTrack, UUID> {
    List<MediumTermTrack> findAllByUserOrderByRank(User user);
    void deleteByUser(User user);
} 