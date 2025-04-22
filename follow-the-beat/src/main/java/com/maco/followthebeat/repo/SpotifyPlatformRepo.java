package com.maco.followthebeat.repo;

import com.maco.followthebeat.entity.SpotifyPlatform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface SpotifyPlatformRepo extends JpaRepository<SpotifyPlatform, UUID> {
}
