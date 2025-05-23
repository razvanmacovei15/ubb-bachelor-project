package com.maco.followthebeat.v2.spotify.artists.repo;

import com.maco.followthebeat.v2.user.entity.User;
import com.maco.followthebeat.v2.spotify.artists.entity.MediumTermArtist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MediumTermArtistRepository extends JpaRepository<MediumTermArtist, UUID> {
    List<MediumTermArtist> findAllByUserOrderByRank(User user);
    void deleteByUser(User user);
} 