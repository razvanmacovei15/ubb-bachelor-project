package com.maco.followthebeat.v2.core.repo;

import com.maco.followthebeat.v2.core.entity.Concert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface ConcertRepo extends JpaRepository<Concert, UUID>, JpaSpecificationExecutor<Concert> {
}
