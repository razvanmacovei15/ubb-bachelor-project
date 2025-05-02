package com.maco.followthebeat.v2.core.repo;

import com.maco.followthebeat.v2.core.entity.Festival;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface FestivalRepo extends JpaRepository<Festival, UUID> {
}
