package com.maco.followthebeat.repo.spotify;

import com.maco.followthebeat.entity.User;
import com.maco.followthebeat.entity.spotify.ShortTermArtist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ShortTermArtistRepository extends JpaRepository<ShortTermArtist, UUID> {
    List<ShortTermArtist> findAllByUserOrderByRank(User user);
    void deleteByUser(User user);
} 