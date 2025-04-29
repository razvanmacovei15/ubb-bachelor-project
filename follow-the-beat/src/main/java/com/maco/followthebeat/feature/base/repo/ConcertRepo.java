package com.maco.followthebeat.feature.base.repo;

import com.maco.followthebeat.feature.base.entity.Concert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface ConcertRepo extends JpaRepository<Concert, UUID> {
}
