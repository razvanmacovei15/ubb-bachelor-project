package com.maco.followthebeat.v2.spotify.artists.repo;

import com.maco.followthebeat.v2.user.entity.User;
import com.maco.followthebeat.v2.spotify.artists.entity.LongTermArtist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LongTermArtistRepository extends JpaRepository<LongTermArtist, UUID> {
    List<LongTermArtist> findAllByUserOrderByRank(User user);
    void deleteByUser(User user);
} 