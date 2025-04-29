package com.maco.followthebeat.feature.base.repo;

import com.maco.followthebeat.feature.base.entity.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VenueRepo extends JpaRepository<Venue, UUID> {
}
